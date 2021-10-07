package lucis.compiler.semantic;

public record LucisVariable(LucisType type, String name) implements LucisElement {
    @Override
    public String signature() {
        return name;
    }
}
