package lucis.compiler.entity;

public class SymbolTable {
    private final SymbolTable parent;

    public SymbolTable() {
        this(null);
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }
}
