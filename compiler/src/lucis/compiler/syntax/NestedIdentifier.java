package lucis.compiler.syntax;

public record NestedIdentifier(String name, NestedIdentifier child) {
    public NestedIdentifier(String name) {
        this(name, null);
    }

    @Override
    public String toString() {
        if (child != null) return name + "." + child;
        return name;
    }
}
