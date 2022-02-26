package lucis.compiler.semantic.concept;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class KindType extends LucisType {
    private final LucisType[] bases;
    private final LucisKind kind;
    private final List<Parameter> parameters;

    public record Parameter(Variant variant, LucisType type) implements Comparable<Parameter> {
        @Override
        public String toString() {
            return switch (variant) {
                case COVARIANT -> type + "+";
                case INVARIANT -> type + "";
                case CONTRAVARIANT -> type + "-";
            };
        }

        @Override
        public int compareTo(Parameter o) {
            return 0;
        }
    }

    public KindType(LucisKind kind, List<Parameter> parameters, LucisType... bases) {
        super(kind.module(), stringify(kind, parameters));
        this.kind = kind;
        this.parameters = parameters;
        this.bases = bases;
    }

    @Override
    public boolean match(LucisType type) {
        return false;
    }

    @Override
    public boolean assignable(LucisType type) {
        if (Objects.equals(type, this)) return true;
        if (Arrays.stream(bases).anyMatch(t -> t.assignable(type))) return true;
        if (type instanceof KindType kindType) {
            if (!Objects.equals(kind, kindType.kind)) return false;
            Iterator<Parameter> iterator = kindType.parameters.iterator();
            for (Parameter p1 : parameters) {
                if (!iterator.hasNext()) return false;
                Parameter p2 = iterator.next();
                boolean flag = switch (p1.variant) {
                    case COVARIANT -> p1.type.assignable(p2.type);
                    case INVARIANT -> Objects.equals(p1.type, p2.type);
                    case CONTRAVARIANT -> p2.type.assignable(p1.type);
                };
                if (!flag) return false;
            }
            return !iterator.hasNext();
        }
        return false;
    }

    private static String stringify(LucisKind kind, List<Parameter> parameters) {
        return kind.name() + "<" + parameters.stream().map(Parameter::toString).reduce((s1, s2) -> s1 + ',' + s2).orElse("") + ">";
    }

    public LucisKind kind() {
        return kind;
    }

    public List<Parameter> parameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return stringify(kind, parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KindType kindType)) return false;
        return Arrays.equals(bases, kindType.bases) && Objects.equals(kind, kindType.kind) && Objects.equals(parameters, kindType.parameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(kind, parameters);
        result = 31 * result + Arrays.hashCode(bases);
        return result;
    }
}
