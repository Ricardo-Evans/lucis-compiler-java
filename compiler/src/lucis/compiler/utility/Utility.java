package lucis.compiler.utility;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public final class Utility {
    private Utility() {
    }

    public static String escape(String s) {
        StringBuilder builder = new StringBuilder();
        s.codePoints().forEach(new IntConsumer() {
            private boolean escape = false;

            @Override
            public void accept(int codepoint) {
                if (escape) {
                    switch (codepoint) {
                        case 'b' -> builder.appendCodePoint('\b');
                        case 'f' -> builder.appendCodePoint('\f');
                        case 'n' -> builder.appendCodePoint('\n');
                        case 'r' -> builder.appendCodePoint('\r');
                        case 't' -> builder.appendCodePoint('\t');
                        case '\\' -> builder.appendCodePoint('\\');
                        case '"' -> builder.appendCodePoint('"');
                    }
                    escape = false;
                } else {
                    if (codepoint != '\\') builder.appendCodePoint(codepoint);
                    else escape = true;
                }
            }
        });
        return builder.toString();
    }

    public static String signature(String returnType, Iterable<String> parameterTypes) {
        StringBuilder builder = new StringBuilder();
        parameterTypes.forEach(s -> builder.append(s).append("->"));
        builder.append(returnType);
        return builder.toString();
    }

}
