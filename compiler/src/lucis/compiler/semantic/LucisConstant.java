package lucis.compiler.semantic;

// declared type, declared name and bound value
public record LucisConstant(LucisType type, String name, LucisObject value) implements LucisElement {
    @Override
    public String signature() {
        return null;
    }
}
