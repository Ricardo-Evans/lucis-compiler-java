package lucis.compiler.semantic.concept;

public class WildcardType extends BasicType {
    public static final WildcardType instance = new WildcardType();

    private WildcardType() {
        super(null, "?", "?");
    }

    @Override
    public boolean match(LucisType type) {
        return false;
    }

    @Override
    public boolean assignable(LucisType type) {
        return false;
    }
}
