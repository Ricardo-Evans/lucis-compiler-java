package lucis.compiler.syntax;

public record UniqueIdentifier(String module, String name) {
    public UniqueIdentifier(String name) {
        this(null, name);
    }
}
