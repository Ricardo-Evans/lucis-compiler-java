package compiler.entity;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Used as the intermediate medium between IO operations and serialize/deserialize operations
 *
 * @author feax
 * @author Ricardo Evans
 * @version 1.1
 */
public class BinaryData {
    private final List<byte[]> buckets;
    private static final int bucketSize = 1 << 10;
    private Mode mode = Mode.WRITE;
    private int offset = 0; // subscript, point to the byte haven't read yet
    private int limit = 0;

    private enum Mode {
        READ, WRITE
    }

    private BinaryData(int initialBuckets) {
        this.buckets = new ArrayList<>(initialBuckets);
        Stream.generate(() -> new byte[bucketSize]).limit(initialBuckets).forEach(buckets::add);
    }

    public static BinaryData wrap(byte[] data) {
        BinaryData result = new BinaryData((data.length + bucketSize - 1) / bucketSize);
        result.writeNBytes(data);
        result.prepareForRead();
        return result;
    }

    public static BinaryData allocate(int size) {
        return new BinaryData((size + bucketSize - 1) / bucketSize);
    }

    // check whether read index is out of bound
    private void readCheck(int size) {
        if (size < 0) throw new IllegalArgumentException("read size cannot be negative");
        if (mode != Mode.READ) throw new IllegalStateException("read in write mode");
        if (size + offset >= limit) throw new IndexOutOfBoundsException(size + offset);
    }

    // check whether write index is out of bound
    private void writeCheck(int size) {
        if (size < 0) throw new IllegalArgumentException("write size cannot be negative");
        if (mode != Mode.WRITE) throw new IllegalStateException("cannot write in read mode!");
    }

    // make sure the underlying buckets can hold more data of the given size, expand if necessary
    private void expand(int size) {
        long expandSize = size + offset - limit;
        if (expandSize <= 0) return;
        Stream.generate(() -> new byte[bucketSize]).limit((expandSize + bucketSize - 1) / bucketSize).forEach(buckets::add);
        limit = bucketSize * buckets.size();
    }

    public byte read() {
        readCheck(1);
        return buckets.get(offset / bucketSize)[offset++ % bucketSize];
    }

    public void write(byte b) {
        writeCheck(1);
        expand(1);
        buckets.get(offset / bucketSize)[offset++ % bucketSize] = b;
    }

    public byte[] readNBytes(int size) {
        readCheck(size);
        byte[] result = new byte[size];
        int readBytes = 0;
        while (readBytes < size) {
            int batchSize = Math.min(bucketSize - offset % bucketSize, size - readBytes);
            System.arraycopy(buckets.get(offset / bucketSize), offset % bucketSize, result, readBytes, batchSize);
            readBytes += batchSize;
            offset += batchSize;
        }
        return result;
    }

    public void writeNBytes(byte[] data) {
        int size = data.length;
        writeCheck(size);
        expand(size);
        int writeBytes = 0;
        while (writeBytes < size) {
            int batchSize = Math.min(bucketSize - offset % bucketSize, size - writeBytes);
            System.arraycopy(data, writeBytes, buckets.get(offset / bucketSize), offset % bucketSize, batchSize);
            writeBytes += batchSize;
            offset += batchSize;
        }
    }

    public String readString(int n, Charset charset) {
        byte[] bytes = readNBytes(n); // check index in getNBytes
        return new String(bytes, offset, n, charset);
    }

    public void writeString(String s, Charset charset) {
        writeNBytes(s.getBytes(charset));
    }

    public int readInteger32() {
        byte[] bytes = readNBytes(Integer.BYTES);
        int result = 0;
        for (int i = 0; i < Integer.BYTES; ++i) {
            result <<= Byte.SIZE;
            result |= bytes[i];
        }
        return result;
    }

    public void writeInteger32(int data) {
        byte[] bytes = new byte[Integer.BYTES];
        for (int i = Integer.BYTES - 1; i >= 0; --i) {
            bytes[i] = (byte) data;
            data >>= Byte.SIZE;
        }
        writeNBytes(bytes);
    }

    public long readInteger64() {
        byte[] bytes = readNBytes(Long.BYTES);
        long result = 0;
        for (int i = 0; i < Long.BYTES; ++i) {
            result <<= Byte.SIZE;
            result |= bytes[i];
        }
        return result;
    }

    public void writeInteger64(long data) {
        byte[] bytes = new byte[Long.BYTES];
        for (int i = Long.BYTES - 1; i >= 0; --i) {
            bytes[i] = (byte) data;
            data >>= Byte.SIZE;
        }
        writeNBytes(bytes);
    }

    public float readDecimal32() {
        return Float.intBitsToFloat(readInteger32());
    }

    public void writeDecimal32(float data) {
        writeInteger32(Float.floatToIntBits(data));
    }

    public double readDecimal64() {
        return Double.longBitsToDouble(readInteger64());
    }

    public void writeDecimal64(double data) {
        writeInteger64(Double.doubleToLongBits(data));
    }

    public <T> T readObject(Function<BinaryData, T> reader) {
        return reader.apply(this);
    }

    public void writeObject(Consumer<BinaryData> writer) {
        writer.accept(this);
    }

    public void prepareForRead() {
        offset = 0;
        limit = bucketSize * buckets.size();
        mode = Mode.READ;
    }

    public void prepareForWrite() {
        limit = offset;
        offset = 0;
        mode = Mode.WRITE;
    }
}
