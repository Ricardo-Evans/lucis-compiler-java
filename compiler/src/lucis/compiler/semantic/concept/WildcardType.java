package lucis.compiler.semantic.concept;

public class WildcardType extends LucisType {
    public static final WildcardType instance = new WildcardType();

    private WildcardType() {
        super("?", "?");
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
