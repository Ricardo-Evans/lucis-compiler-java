package lucis.compiler.semantic;

import compiler.semantic.SemanticException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class LucisModule implements Serializable {
    @Serial
    private static final long serialVersionUID = 8968825529218018034L;
    public final String name;
    private final Map<String, Set<LucisSymbol>> symbols = new HashMap<>();
    private final Map<String, LucisType> types = new HashMap<>();
    private final Map<LucisObject, Integer> constants = new HashMap<>();
    private final Map<LucisSymbol, LucisVariable> variables = new HashMap<>();

    public LucisModule(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public Map<String, Set<LucisSymbol>> symbols() {
        return symbols;
    }

    public Map<String, LucisType> types() {
        return types;
    }

    public Set<LucisSymbol> findSymbol(String name) {
        return symbols.getOrDefault(name, Set.of());
    }

    public void foundSymbol(LucisSymbol symbol) {
        foundSymbol(symbol.name(), symbol);
    }

    public void foundSymbol(String name, LucisSymbol symbol) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(symbol);
        symbols.putIfAbsent(name, new HashSet<>());
        if (!symbols.get(name).add(symbol)) throw new SemanticException(symbol + " is already defined");
    }

    public Optional<LucisType> findType(String name) {
        Objects.requireNonNull(name);
        return Optional.ofNullable(types.get(name));
    }

    public void foundType(LucisType type) {
        Objects.requireNonNull(type);
        String name = type.name();
        if (types.containsKey(name)) throw new SemanticException(""); // TODO
        types.put(name, type);
    }
}
