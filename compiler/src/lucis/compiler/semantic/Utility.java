package lucis.compiler.semantic;

import lucis.compiler.semantic.concept.Element;
import lucis.compiler.semantic.concept.KindType;
import lucis.compiler.semantic.concept.LucisKind;
import lucis.compiler.semantic.concept.LucisType;

import java.util.Objects;
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

    public static boolean fromKind(LucisType type, LucisKind kind) {
        if (!(type instanceof KindType kindType)) return false;
        return Objects.equals(kindType.kind(), kind);
    }

    public static final Function<Stream<Element>, Stream<Element>> NoFilter = s -> s;

    // public static final Function<Stream<Element>, Stream<Element>> TypeFilter = s -> s.filter(e -> e.type().is());
}
