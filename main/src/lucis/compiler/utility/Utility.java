package lucis.compiler.utility;

import java.util.function.IntConsumer;

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
                        case 'b': {
                            builder.appendCodePoint('\b');
                            break;
                        }
                        case 'f': {
                            builder.appendCodePoint('\f');
                            break;
                        }
                        case 'n': {
                            builder.appendCodePoint('\n');
                            break;
                        }
                        case 'r': {
                            builder.appendCodePoint('\r');
                            break;
                        }
                        case 't': {
                            builder.appendCodePoint('\t');
                            break;
                        }
                        case '\\': {
                            builder.appendCodePoint('\\');
                            break;
                        }
                        case '"': {
                            builder.appendCodePoint('"');
                            break;
                        }
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
