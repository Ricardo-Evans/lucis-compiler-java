package lucis.compiler.semantic;

import compiler.semantic.SemanticException;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Context {
    private LucisModule currentModule;
    private final Context parent;
    private final Map<String, LucisSymbol> symbols = new HashMap<>();
    private final Map<String, Set<LucisSymbol>> ambitious = new HashMap<>();

    public Context() {
        this.parent = null;
    }

    public Context(Context parent) {
        Objects.requireNonNull(parent);
        this.parent = parent;
    }

    public Context root() {
        return parent == null ? this : parent.root();
    }

    public Optional<Context> parent() {
        return Optional.ofNullable(parent);
    }

    public LucisElement findElement(String name, Function<Stream<LucisElement>, Stream<LucisElement>> filter) {
        return Optional.ofNullable(symbols.get(name)).stream()
                .flatMap(s -> s.findElement(filter))
                .reduce(Utility.throwOnInvoke(() -> new SemanticException("multiple candidate found for " + name)))
                .or(() -> parent().map(c -> c.findElement(name, filter)))
                .orElseThrow(() -> new SemanticException("no candidate found for " + name));
    }

    public void foundElement(String name, LucisElement element) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(element);
        symbols.get(name).foundElement(element);
    }

    public Optional<LucisModule> getCurrentModule() {
        return Optional.ofNullable(currentModule).or(() -> parent().flatMap(Context::getCurrentModule));
    }

    public void setCurrentModule(LucisModule module) {
        Objects.requireNonNull(module);
        if (this.currentModule != null)
            throw new SemanticException("cannot define module " + module + " in module " + this.currentModule.name());
        this.currentModule = module;
    }

    public LucisModule requireCurrentModule() {
        return getCurrentModule().orElseThrow(() -> new SemanticException("no module defined"));
    }

    public void importSymbol(LucisSymbol symbol) {
        String signature = symbol.signature();
        if (symbols.put(signature, symbol) != null)
            throw new SemanticException(signature + " is imported but already exist");
        String name = symbol.name();
        if (ambitious.containsKey(name)) ambitious.get(name).add(symbol);
        if (symbols.containsKey(name)) {
            ambitious.putIfAbsent(name, new HashSet<>());
            ambitious.get(name).add(symbols.remove(name));
            ambitious.get(name).add(symbol);
        } else symbols.put(name, symbol);
    }

    public void importModule(LucisModule module) {
        Objects.requireNonNull(module);
        module.symbols().forEach((name, symbol) -> importSymbol(symbol));
    }

    public void importCurrentModule() {
        importModule(requireCurrentModule());
    }
}
