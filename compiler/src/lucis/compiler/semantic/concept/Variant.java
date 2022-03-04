package lucis.compiler.semantic.concept;

public enum Variant {
    COVARIANT,
    INVARIANT,
    CONTRAVARIANT;

    @Override
    public String toString() {
        return switch (this) {
            case INVARIANT -> "";
            case COVARIANT -> "+";
            case CONTRAVARIANT -> "-";
        };
    }
}
