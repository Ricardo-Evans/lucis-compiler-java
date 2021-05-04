package lucis.compiler.semantic;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final SymbolTable parent;
    private final Map<String, Symbol> table = new HashMap<>();

    public SymbolTable() {
        this(null);
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public SymbolTable parent() {
        return parent;
    }

    public Symbol get(String name) {
        if (parent == null) return table.get(name);
        return table.getOrDefault(name, parent.get(name));
    }

    public void set(String name, Symbol symbol) {
        table.putIfAbsent(name, symbol);
    }
}
