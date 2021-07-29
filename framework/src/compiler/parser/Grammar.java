package compiler.parser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public record Grammar(String left, Part[] right, Function<Object[], ?> reduction) implements Serializable {
    @Serial
    private static final long serialVersionUID = -682497989112307167L;

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

    public int length() {
        return right.length;
    }

    @Override
    public String toString() {
        return left + ":" + Arrays.toString(right);
    }
}
