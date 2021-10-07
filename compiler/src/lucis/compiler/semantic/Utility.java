package lucis.compiler.semantic;

import java.util.StringJoiner;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class Utility {
    public static final String LUCIS_CORE = "lucis.core";

    private Utility() {
    }

    public static String calculateSignature(LucisType result, LucisType... parameters) {
        StringJoiner joiner = new StringJoiner(",");
        for (LucisType type : parameters) {
            joiner.add(type.signature());
        }
        return result.signature() + '(' + joiner + ')';
    }

    public static <T> BinaryOperator<T> throwOnInvoke(Supplier<? extends RuntimeException> supplier) {
        return (o1, o2) -> {
            throw supplier.get();
        };
    }

    public static Function<Stream<LucisElement>, Stream<LucisElement>> noFilter() {
        return s -> s;
    }
}
