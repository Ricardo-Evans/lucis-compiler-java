package compiler.entity;

import java.nio.charset.Charset;
import java.util.Arrays;

public class BinaryData {
    private final byte[] array;
    private int offset;
    private int available;

    public BinaryData(int size) {
        this(new byte[size]);
    }

    public BinaryData(byte[] array) {
        this(array, array.length);
    }

    public BinaryData(byte[] array, int size) {
        this(array, 0, size);
    }

    public BinaryData(byte[] array, int offset, int size) {
        this.array = array;
        this.offset = offset;
        this.available = size;
    }

    public void shift(int n) {
        if (n > available || n + offset < 0) throw new IndexOutOfBoundsException(n);
        offset += n;
        available -= n;
    }

    public byte get(int n) {
        if (n >= available) throw new IndexOutOfBoundsException(n);
        return array[offset + n];
    }

    public void set(byte b, int n) {
        if (n >= available) throw new IndexOutOfBoundsException(n);
        array[offset + n] = b;
    }

    public byte get() {
        if (offset >= available) throw new IndexOutOfBoundsException();
        byte result = array[offset];
        ++offset;
        --available;
        return result;
    }

    public void set(byte b) {
        if (offset >= available) throw new IndexOutOfBoundsException();
        array[offset] = b;
        ++offset;
        --available;
    }

    public byte[] array() {
        return array;
    }

    public int offset() {
        return offset;
    }

    public int available() {
        return available;
    }

    public byte[] getNBytes(int n) {
        if (n > available) throw new IndexOutOfBoundsException(n);
        byte[] result = new byte[n];
        System.arraycopy(array, offset, result, 0, n);
        offset += n;
        available -= n;
        return result;
    }

    public void setNBytes(byte[] data) {
        int n = data.length;
        if (n > available) throw new IndexOutOfBoundsException(n);
        System.arraycopy(data, 0, array, offset, n);
        offset += n;
        available -= n;
    }

    public String getString(int n, Charset charset) {
        if (n > available) throw new IndexOutOfBoundsException(n);
        String result = new String(array, offset, n, charset);
        offset += n;
        available -= n;
        return result;
    }

    public void setString(String s, Charset charset) {
        setNBytes(s.getBytes(charset));
    }

    public int getInteger32() {
        if (available < Integer.BYTES) throw new IndexOutOfBoundsException(Integer.BYTES);
        int result = 0;
        for (int i = 0; i < Integer.BYTES; ++i) {
            result <<= Byte.SIZE;
            result |= get();
        }
        return result;
    }

    public void setInteger32(int data) {
        if (available < Integer.BYTES) throw new IndexOutOfBoundsException(Integer.BYTES);
        for (int i = Integer.BYTES - 1; i >= 0; --i) {
            set((byte) data, i);
            data >>= Byte.SIZE;
        }
        offset += Integer.BYTES;
        available -= Integer.BYTES;
    }

    public long getInteger64() {
        if (available < Integer.BYTES) throw new IndexOutOfBoundsException(Long.BYTES);
        long result = 0;
        for (int i = 0; i < Long.BYTES; ++i) {
            result <<= Byte.SIZE;
            result |= get(i);
        }
        offset += Long.BYTES;
        available -= Long.BYTES;
        return result;
    }

    public void setInteger64(long data) {
        if (available < Long.BYTES) throw new IndexOutOfBoundsException(Long.BYTES);
        for (int i = Long.BYTES - 1; i >= 0; --i) {
            set((byte) data, i);
            data >>= Byte.SIZE;
        }
        offset += Long.BYTES;
        available -= Long.BYTES;
    }

    public float getDecimal32() {
        return Float.intBitsToFloat(getInteger32());
    }

    public void setDecimal32(float data) {
        setInteger32(Float.floatToIntBits(data));
    }

    public double getDecimal64() {
        return Double.longBitsToDouble(getInteger64());
    }

    public void setDecimal64(double data) {
        setInteger64(Double.doubleToLongBits(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryData that)) return false;
        return Arrays.equals(array, offset, offset + available, that.array, that.offset, that.offset + that.available);
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = offset; i < offset + available; ++i) result = 31 * result + array[i];
        return result;
    }
}
