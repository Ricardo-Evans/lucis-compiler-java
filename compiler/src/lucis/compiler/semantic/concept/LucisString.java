package lucis.compiler.semantic.concept;

import java.util.Arrays;

public class LucisString implements LucisObject {
    public final byte[] bytes;

    public LucisString(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean equals(java.lang.Object o) {
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
