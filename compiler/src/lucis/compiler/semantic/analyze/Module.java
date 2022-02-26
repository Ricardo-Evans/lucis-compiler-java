package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.LucisObject;
import lucis.compiler.semantic.concept.LucisType;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

public class Module implements Serializable {
    @Serial
    private static final long serialVersionUID = 8968825529218018034L;
    private final String name;
    private final Map<String, Set<Symbol>> symbols = new HashMap<>();

    public Module(String name) {
        Objects.requireNonNull(name);
        this.name = name;
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
