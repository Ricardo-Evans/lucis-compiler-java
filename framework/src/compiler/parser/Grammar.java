package compiler.parser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class Grammar implements Serializable {
    @Serial
    private static final long serialVersionUID = -682497989112307167L;
    public final String left;
    public final Part[] right;
    public final Function<Object[], ?> reduction;

    public enum Capture {
        INCLUDE,
        EXCLUDE,
        DEFAULT,
    }

    public static class Part {
        public final String name;
        public final Capture capture;

        public Part(String name, Capture capture) {
            this.name = name;
            this.capture = capture;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Part part)) return false;
            return Objects.equals(name, part.name) && capture == part.capture;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, capture);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public Grammar(String left, String[] right, Function<Object[], ?> reduction) {
        this(left, Arrays.stream(right).map(s -> new Part(s, Capture.DEFAULT)).toArray(Part[]::new), reduction);
    }

    public Grammar(String left, Part[] right, Function<Object[], ?> reduction) {
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
