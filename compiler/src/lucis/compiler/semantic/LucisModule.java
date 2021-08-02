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
    private final Map<LucisSymbol, LucisModule> modules = new HashMap<>();
    private final Map<LucisSymbol, LucisType> types = new HashMap<>();
    private final Map<LucisSymbol, LucisFunction> functions = new HashMap<>();
    private final Map<LucisObject, Integer> constants = new HashMap<>();
    private final Map<LucisSymbol, LucisVariable> variables = new HashMap<>();

    public LucisModule(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public Set<LucisSymbol> findSymbol(String name) {
        return symbols.getOrDefault(name, Set.of());
    }

    public void foundSymbol(String name, LucisSymbol symbol) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(symbol);
        symbols.putIfAbsent(name, new HashSet<>());
        if (!symbols.get(name).add(symbol)) throw new SemanticException("symbol " + symbol + " is already defined");
    }

    public Optional<LucisModule> findModule(LucisSymbol symbol) {
        Objects.requireNonNull(symbol);
        return Optional.ofNullable(modules.get(symbol));
    }

    public void foundModule(LucisSymbol symbol, LucisModule module) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(module);
        if (modules.containsKey(symbol)) throw new SemanticException("module " + symbol + " is already defined");
        modules.put(symbol, module);
    }

    public Optional<LucisType> findType(LucisSymbol symbol, LucisType type) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(type);
        return Optional.ofNullable(types.get(symbol));
    }

    public void foundType(LucisSymbol symbol, LucisType type) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(type);
        if (types.containsKey(symbol)) throw new SemanticException("type: " + symbol + " already defined");
        types.put(symbol, type);
    }
}
