package lucis.compiler.semantic.concept;

public record Variable(LucisType type, String name) implements Element {
    @Override
    public String signature() {
        return name;
    }
}
