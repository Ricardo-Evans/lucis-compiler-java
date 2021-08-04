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

    public boolean foundSymbol(LucisSymbol symbol) {
        return foundSymbol(symbol.name(), symbol);
    }

    public boolean foundSymbol(String name, LucisSymbol symbol) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(symbol);
        symbols.putIfAbsent(name, new HashSet<>());
        return symbols.get(name).add(symbol);
    }

    public Optional<LucisType> findType(LucisSymbol symbol) {
        Objects.requireNonNull(symbol);
        return Optional.ofNullable(types.get(symbol));
    }

    public LucisSymbol foundType(LucisType type) {
        LucisSymbol symbol = new LucisSymbol(type.name(), name, LucisSymbol.Kind.TYPE);
        foundType(symbol, type);
        return symbol;
    }

    public void foundType(LucisSymbol symbol, LucisType type) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(type);
        if (types.containsKey(symbol)) throw new SemanticException("type: " + symbol + " already defined");
        types.put(symbol, type);
        foundSymbol(symbol);
    }

    public Optional<LucisFunction> findFunction(LucisSymbol symbol) {
        Objects.requireNonNull(symbol);
        return Optional.ofNullable(functions.get(symbol));
    }

    public LucisSymbol foundFunction(LucisFunction function) {
        LucisSymbol symbol = new LucisSymbol(function.name, name, function.signature, LucisSymbol.Kind.FUNCTION);
        foundFunction(symbol, function);
        return symbol;
    }

    public void foundFunction(LucisSymbol symbol, LucisFunction function) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(function);
        if (functions.containsKey(symbol)) throw new SemanticException("function: " + function + " already defined");
        functions.put(symbol, function);
        foundSymbol(symbol);
    }
}
