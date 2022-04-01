package compiler.entity;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class BinaryData {
    private final int basicSize = 512;
    private ArrayList<byte[]> byteList;
    private int offset; // subscript, point to the byte haven't read yet
    private int size;
    private final int[] use; // point to the next unwritten byte, use[1]:0~511
    private int mode; // 0 means read, 1 means write

    // deprecated field, delete after review
    //    private int available;
//    private int readAvailable; // change after writing and after offset change, use - offset
//    private int writeAvailable; // change after expand and after offset change, size - offset


    public BinaryData() {
        byteList = new ArrayList<>();
        byteList.add(new byte[basicSize]);
        use = new int[2];
        //use[0]=0;use[1]=0;
        offset = 0;
        size = basicSize;
    }

    public BinaryData(int s) {
        byteList = new ArrayList<>();
        use = new int[2];
        offset = 0;
        while (s >= 0) {
            s -= basicSize;
            byteList.add(new byte[basicSize]);
            size += basicSize;
        }
    }

    // private basic tool functions=================================================================

    // n: subscript, not number
    private int[] getLocation(int n) {
        return new int[]{n / basicSize, n - n / basicSize};
    }

    // return number of written bytes
    public int getUse() {
        return use[0] * basicSize + use[1];
    }
    private void updateUse(int n) {
        if (n + offset > getUse()) {
            int[] location = getLocation(offset + n);
            use[0] = location[0];
            use[1] = location[1];
        }
    }

    private void checkReadIndex(int n) {
        if (mode != 0) throw new IllegalStateException("cannot read in write mode!");
        if (n + offset >= getUse() - 1 || n + offset < 0) throw new IndexOutOfBoundsException(n);
    }

    private int checkWriteIndexAndExpand(int n) {
        if (mode != 1) throw new IllegalStateException("cannot write in read mode!");
        if (n + offset < 0) throw new IndexOutOfBoundsException(n);
        if (n + offset < size) return 0; // if exactly equal, expand, too. or field 'use' will fail update

        int needArray = (n + offset) / basicSize + 1 - byteList.size();
        for (int i = 0; i < needArray; ++i) {
            byteList.add(new byte[basicSize]);
            size += basicSize;
        }
        return needArray;
    }

    private void expandSimplified() {
        byteList.add(new byte[basicSize]);
        size += basicSize;
    }

    private void shift(int n) {
        if (n + offset >= size || n + offset < 0) throw new IndexOutOfBoundsException(n);
        offset += n;
    }

    private byte get(int n) {
        checkReadIndex(n);
        int[] location = getLocation(offset + n);
        offset += n;
        return byteList.get(location[0])[location[1]];
    }
    private void set(byte b, int n) {
        checkWriteIndexAndExpand(n);
        int[] location = getLocation(offset + n);
        offset += n;
        byteList.get(location[0])[location[1]] = b;
        updateUse(n);
    }

    // public functions===========================================================================


    public int[] use() {
        return use;
    }

    public byte get() {
        return get(1);
    }
    public void set(byte b) {
        set(b, 1);
    }

    public byte[] getNBytes(int n) {
        checkReadIndex(n);
        byte[] result = new byte[n];
        int[] location = getLocation(n + offset);

        if (n <= basicSize - location[1]) {
            System.arraycopy(byteList.get(location[0]), location[1], result, 0, n);
            offset += n;
            return result;
        }

        int finish = 0;
        System.arraycopy(byteList.get(location[0]), location[1], result, finish, basicSize - location[1]);
        finish += basicSize - location[1];
        while (finish < n) {
            location[0] += 1;
            if (n - finish <= basicSize) {
                System.arraycopy(byteList.get(location[0]), 0, result, finish, n - finish);
                break;
            }
            System.arraycopy(byteList.get(location[0]), 0, result, finish, basicSize);
            finish += basicSize;
        }

        offset += n;
        return result;
    }
    public void setNBytes(byte[] data) {
        int n = data.length;
        checkWriteIndexAndExpand(n);
        int[] location = getLocation(n + offset);

        if (n <= basicSize - location[1]) {
            System.arraycopy(data, 0, byteList.get(location[0]), location[1], n);
            offset += n;
            updateUse(n);
            return;
        }

        int finish = 0;
        System.arraycopy(data, finish, byteList.get(location[0]), location[1], basicSize - location[1]);
        finish += basicSize - location[1];
        while (finish < n) {
            location[0] += 1;
            if (n - finish <= basicSize) {
                System.arraycopy(data, finish, byteList.get(location[0]), 0, n - finish);
                break;
            }
            System.arraycopy(data, finish, byteList.get(location[0]), 0, basicSize);
            finish += basicSize;
        }
        offset += n;
        updateUse(n);
    }

    public String getString(int n, Charset charset) {
        byte[] bytes = getNBytes(n); // check index in getNBytes
        return new String(bytes, offset, n, charset);
    }
    public void setString(String s, Charset charset) {
        setNBytes(s.getBytes(charset));
    }

    public int getInteger32() {
        byte[] bytes = getNBytes(Integer.BYTES);
        int result = 0;
        for (int i = 0; i < Integer.BYTES; ++i) {
            result <<= Byte.SIZE;
            result |= bytes[i];
        }
        return result;
    }
    public void setInteger32(int data) {
        byte[] bytes = new byte[Integer.BYTES];
        for (int i = Integer.BYTES - 1; i >= 0; --i) {
            bytes[i] = (byte) data;
            data >>= Byte.SIZE;
        }
        setNBytes(bytes);
    }

    public long getInteger64() {
        byte[] bytes = getNBytes(Long.BYTES);
        long result = 0;
        for (int i = 0; i < Long.BYTES; ++i) {
            result <<= Byte.SIZE;
            result |= bytes[i];
        }
        return result;
    }
    public void setInteger64(long data) {
        byte[] bytes = new byte[Long.BYTES];
        for (int i = Long.BYTES - 1; i >= 0; --i) {
            bytes[i] = (byte) data;
            data >>= Byte.SIZE;
        }
        setNBytes(bytes);
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

    public void setWrite() {
        use[0] = 0;
        use[1] = 0;
        offset = 0;
        size = basicSize;
        mode = 1;
        byteList.clear();
        byteList.add(new byte[basicSize]);
    }
    public void setRead() {
        offset = 0;
        mode = 0;
    }

// deprecated
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof BinaryData that)) return false;
//        return Arrays.equals(array, offset, offset + available, that.array, that.offset, that.offset + that.available);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = 1;
//        for (int i = offset; i < offset + available; ++i) result = 31 * result + array[i];
//        return result;
//    }
}
