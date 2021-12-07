package lucis.compiler.semantic.analyze;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolTable {
    private final SymbolTable parent;
    private final Map<String, Symbol> symbols = new HashMap<>();

    public SymbolTable() {
        this(null);
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public Optional<SymbolTable> parent() {
        return Optional.ofNullable(parent);
    }
}
