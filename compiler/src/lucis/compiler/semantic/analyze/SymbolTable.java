package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.LucisType;

import java.util.*;
import java.util.stream.Collectors;

public class SymbolTable {
    private final SymbolTable parent;
    private final Map<String, Set<Symbol>> storage = new HashMap<>();

    public SymbolTable() {
        this(null);
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public Optional<SymbolTable> parent() {
        return Optional.ofNullable(parent);
    }

    public Optional<Symbol> findSymbol(String name, LucisType type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        return Optional.ofNullable(storage.get(name)).orElseThrow().stream()
                .filter(s -> s.type().match(type))
                .reduce(Utility.unique(Utility.ambitiousSymbolsFound(name, type)))
                .or(() -> parent().flatMap(s -> s.findSymbol(name, type)));
    }

    public Set<Symbol> findMultipleSymbols(String name, LucisType type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        return storage.getOrDefault(name, Set.of()).stream().filter(s -> s.type().match(type)).collect(Collectors.toUnmodifiableSet());
    }

    public void foundSymbol(String name, Symbol symbol) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(symbol);
        storage.putIfAbsent(name, new HashSet<>());
        if (!storage.get(name).add(symbol)) throw Utility.symbolAlreadyExist(symbol).get();
    }

    public void foundMultipleSymbols(String name, Iterable<? extends Symbol> symbols) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(symbols);
        storage.putIfAbsent(name, new HashSet<>());
        symbols.forEach(symbol -> {
            if (!storage.get(name).add(symbol)) throw Utility.symbolAlreadyExist(symbol).get();
        });
    }
}
