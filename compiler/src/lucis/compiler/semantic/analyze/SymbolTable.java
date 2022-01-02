package lucis.compiler.semantic.analyze;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    public Optional<Symbol> findSymbol(String name) {
        return Optional.ofNullable(symbols.get(name)).or(() -> parent().flatMap(s -> s.findSymbol(name)));
    }

    public Symbol foundSymbol(String name) {
        Objects.requireNonNull(name);
        symbols.putIfAbsent(name, new Symbol(name));
        return symbols.get(name);
    }
}
