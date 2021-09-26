package lucis.compiler.syntax;

public record UniqueIdentifier(String module, String name) {
    public UniqueIdentifier(String name) {
        this(null, name);
    }

    @Override
    public String toString() {
        return module == null ? name : module + ":" + name;
    }
}
