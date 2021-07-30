package lucis.compiler.syntax;

public record Symbol(String name, Symbol child) {
    public Symbol(String name) {
        this(name, null);
    }

    @Override
    public String toString() {
        if (child != null) return name + "." + child;
        return name;
    }
}
