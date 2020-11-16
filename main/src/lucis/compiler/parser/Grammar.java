package lucis.compiler.parser;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Grammar implements Serializable {
    public final String left;
    public final String[] right;
    public final Reduction reduction;

    public Grammar(String left, String[] right, Reduction reduction) {
        this.left = left;
        this.right = right;
        this.reduction = reduction;
    }

    public int length() {
        return right.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grammar grammar = (Grammar) o;
        return Objects.equals(left, grammar.left) &&
                Arrays.equals(right, grammar.right) &&
                Objects.equals(reduction, grammar.reduction);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(left, reduction);
        result = 31 * result + Arrays.hashCode(right);
        return result;
    }

    @Override
    public String toString() {
        return left + ":" + Arrays.toString(right);
    }
}
