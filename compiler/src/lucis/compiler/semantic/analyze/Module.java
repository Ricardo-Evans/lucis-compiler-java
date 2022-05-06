package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.LucisObject;
import lucis.compiler.semantic.concept.LucisType;

import java.util.*;

public class Module {
    private final String name;
    private final List<LucisObject> constantPool;
    private final Map<String, Set<Symbol>> symbols;

    public Module(String name) {
        this(name, new ArrayList<>(), new HashMap<>());
    }

    public Module(String name, List<LucisObject> constantPool, Map<String, Set<Symbol>> symbols) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(constantPool);
        Objects.requireNonNull(symbols);
        this.name = name;
        this.constantPool = constantPool;
        this.symbols = symbols;
    }

    public String name() {
        return name;
    }

    public Map<String, Set<Symbol>> symbols() {
        return symbols;
    }

    public Symbol findSymbol(String name, LucisType type) {
        if (!symbols.containsKey(name))
            throw new SemanticException("symbol of name " + name + " does not exist in " + this);
        return Optional.ofNullable(symbols.get(name)).orElseThrow(Utility.symbolNotFound(name, type)).stream()
                .filter(s -> s.type().match(type))
                .reduce(Utility.unique(Utility.ambitiousSymbolsFound(name, type)))
                .orElseThrow(Utility.symbolNotFound(name, type));
    }

    public void foundSymbol(String name, Symbol symbol) {
        Objects.requireNonNull(name);
        symbols.putIfAbsent(name, new HashSet<>());
        if (!symbols.get(name).add(symbol)) throw Utility.symbolAlreadyExist(symbol).get();
    }
}
