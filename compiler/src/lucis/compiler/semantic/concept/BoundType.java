package lucis.compiler.semantic.concept;

import java.util.Arrays;
import java.util.Objects;

public class BoundType extends BasicType {
    private LucisType baseline;
    private Bound bound;

    private BoundType(LucisType baseline, Bound bound) {
        super(null, baseline.name() + bound, baseline.signature() + bound);
    }

    @Override
    public boolean match(LucisType type) {
        return switch (type) {
            case BoundType t -> {
                if (bound == Bound.LOWER && t.bound == Bound.LOWER) yield t.baseline.assignable(baseline);
                if (bound == Bound.UPPER && t.bound == Bound.UPPER) yield baseline.assignable(t.baseline);
                yield false;
            }
            case WildcardType ignored -> true;
            default -> false;
        };
    }

    @Override
    public boolean assignable(LucisType type) {
        return switch (type) {
            case BoundType t -> {
                if (bound == Bound.UPPER && t.bound() == Bound.LOWER) yield baseline().assignable(t.baseline());
                yield false;
            }
            case ClassType ignored -> {
                if (bound == Bound.UPPER) yield baseline.assignable(type);
                yield false;
            }
            case KindType ignored -> {
                if (bound == Bound.UPPER) yield baseline.assignable(type);
                yield false;
            }
            case TraitType ignored -> {
                if (bound == Bound.UPPER) yield baseline.assignable(type);
                yield false;
            }
            case WildcardType ignored -> true;
            case UnionType unionType -> Arrays.stream(unionType.types()).anyMatch(this::assignable);
            default -> false;
        };
    }

    public LucisType baseline() {
        return baseline;
    }

    public Bound bound() {
        return bound;
    }

    public static BoundType boundUpper(LucisType baseline) {
        Objects.requireNonNull(baseline);
        return new BoundType(baseline, Bound.UPPER);
    }

    public static BoundType boundLower(LucisType baseline) {
        Objects.requireNonNull(baseline);
        return new BoundType(baseline, Bound.LOWER);
    }

    /**
     * Specify the bound type of the constraint
     */
    public enum Bound {
        /**
         * types match the constraint iff they are supertypes of the baseline type
         */
        LOWER,

        /**
         * types match the constraint iff they are subtypes of the baseline type
         */
        UPPER;

        @Override
        public String toString() {
            return switch (this) {
                case LOWER -> "-";
                case UPPER -> "+";
            };
        }
    }
}
