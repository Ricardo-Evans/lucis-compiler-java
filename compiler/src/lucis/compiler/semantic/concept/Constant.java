package lucis.compiler.semantic.concept;

// declared type, declared name and bound value
public record Constant(LucisType type, String name, LucisObject value) implements Element {
    @Override
    public String signature() {
        return null;
    }
}
