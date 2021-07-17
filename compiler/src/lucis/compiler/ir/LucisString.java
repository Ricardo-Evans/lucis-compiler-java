package lucis.compiler.ir;

import lucis.compiler.utility.Constants;

import java.util.Arrays;

public class LucisString extends LucisObject {
    public final byte[] bytes;

    public LucisString(byte[] bytes) {
        super(Constants.STRING_TYPE);
        this.bytes = bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LucisString string = (LucisString) o;
        return Arrays.equals(bytes, string.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
