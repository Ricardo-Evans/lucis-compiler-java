package lucis.compiler.syntax;

public class Symbol {
    public final String name;
    public final Symbol child;

    public Symbol(String name, Symbol child) {
        this.child = child;
        this.name = name;
    }

    public Symbol(String name) {
        this(name, null);
    }
}
