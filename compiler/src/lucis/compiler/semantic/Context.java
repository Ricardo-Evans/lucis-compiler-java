package lucis.compiler.semantic;

import compiler.semantic.SemanticException;

import java.util.*;

public class Context {
    private LucisModule currentModule;
    private Context parent;
    private final Map<String, Set<LucisSymbol>> symbols = new HashMap<>();
    private final Map<String, LucisType> typeMap = new HashMap<>();
    private final Map<String, LucisVariable> variableMap = new HashMap<>();

    public Context(Context parent) {
        this.parent = parent;
    }

    public Context root() {
        return parent().map(Context::root).orElse(this);
    }

    public void parent(Context context) {
        this.parent = context;
    }

    public Optional<Context> parent() {
        return Optional.ofNullable(parent);
    }

    public Set<LucisSymbol> findSymbol(String name) {
        Objects.requireNonNull(name);
        return Optional.ofNullable(symbols.get(name)).or(() -> parent().map(context -> context.findSymbol(name))).orElseGet(Set::of);
    }

    public void foundSymbol(LucisSymbol symbol) {
        foundSymbol(symbol.name(), symbol);
    }

    public void foundSymbol(String name, LucisSymbol symbol) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(symbol);
        symbols.putIfAbsent(name, new HashSet<>());
        if (!symbols.get(name).add(symbol)) throw new SemanticException("symbol " + symbol + " is already defined");
    }

    public Optional<LucisType> findType(String name) {
        return Optional.ofNullable(typeMap.get(name)).or(() -> parent().flatMap(c -> c.findType(name)));
    }

    public void foundType(String name, LucisType type) {
        if (typeMap.containsKey(name)) throw new SemanticException("type " + name + " is already defined");
        typeMap.put(name, type);
    }

    public Optional<LucisModule> getCurrentModule() {
        return Optional.ofNullable(currentModule).or(() -> parent().flatMap(Context::getCurrentModule));
    }

    public void setCurrentModule(LucisModule module) {
        Objects.requireNonNull(module);
        if (this.currentModule != null)
            throw new SemanticException("cannot define module " + module + " in module " + this.currentModule.name);
        this.currentModule = module;
    }

    public Optional<LucisVariable> findVariable(String name) {
        return Optional.ofNullable(variableMap.get(name)).or(() -> parent().flatMap(c -> c.findVariable(name)));
    }

    public void foundVariable(String name, LucisVariable variable) {
        if (variableMap.containsKey(name)) throw new SemanticException("variable " + name + " is already defined");
        variableMap.put(name, variable);
    }

    public void importSymbols(String name, Set<LucisSymbol> symbols) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(symbols);
        this.symbols.putIfAbsent(name, new HashSet<>());
        this.symbols.get(name).addAll(symbols);
    }

    public void importCurrentModule() {
        if (currentModule == null) throw new SemanticException("no module statement found");
        currentModule.symbols().forEach(this::importSymbols);
    }
}
