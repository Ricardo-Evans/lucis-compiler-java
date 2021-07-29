package lucis.compiler.syntax;

public record Symbol(String name,Symbol child) {
    public Symbol(String name) {
        this(name, null);
    }
}
