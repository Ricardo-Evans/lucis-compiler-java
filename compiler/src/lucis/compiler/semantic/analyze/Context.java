package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.concept.Element;
import lucis.compiler.semantic.Utility;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Context {
    private Module currentModule;
    private final Context parent;
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final Map<String, Set<Symbol>> ambitious = new HashMap<>();

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

    public Element findElement(String name, Function<Stream<Element>, Stream<Element>> filter) {
        return Optional.ofNullable(symbols.get(name)).stream()
                .flatMap(s -> s.findElement(filter))
                .reduce(Utility.throwOnInvoke(() -> new SemanticException("multiple candidate found for " + name)))
                .or(() -> parent().map(c -> c.findElement(name, filter)))
                .orElseThrow(() -> new SemanticException("no candidate found for " + name));
    }

    public void foundElement(String name, Element element) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(element);
        symbols.get(name).foundElement(element);
    }

    public Optional<Module> getCurrentModule() {
        return Optional.ofNullable(currentModule).or(() -> parent().flatMap(Context::getCurrentModule));
    }

    public void setCurrentModule(Module module) {
        Objects.requireNonNull(module);
        if (this.currentModule != null)
            throw new SemanticException("cannot define module " + module + " in module " + this.currentModule.name());
        this.currentModule = module;
    }

    public Module requireCurrentModule() {
        return getCurrentModule().orElseThrow(() -> new SemanticException("no module defined"));
    }

    public void importSymbol(Symbol symbol) {
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

    public void importModule(Module module) {
        Objects.requireNonNull(module);
        module.symbols().forEach((name, symbol) -> importSymbol(symbol));
    }

    public void importCurrentModule() {
        importModule(requireCurrentModule());
    }

}
