package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import lucis.compiler.syntax.UniqueIdentifier;

import java.util.*;

public class Context {
    private LucisModule rootModule;
    private LucisModule currentModule;
    private Context parent;
    private final Map<String, Set<LucisSymbol>> symbols = new HashMap<>();
    private final Map<String, LucisType> typeMap = new HashMap<>();
    private final Map<String, LucisVariable> variableMap = new HashMap<>();
    private final Set<LucisModule> importedModules = new HashSet<>();

    public Context(LucisModule rootModule) {
        this((Context) null);
        this.rootModule = rootModule;
    }

    public Context(Context parent) {
        if (parent != null) {
            this.parent = parent;
            this.rootModule = parent.rootModule;
        }
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

    public void setCurrentModule(UniqueIdentifier identifier) {
        if (this.currentModule != null)
            throw new SemanticException("cannot define module " + identifier + " in module " + this.currentModule.name);
        LucisModule module = rootModule;
        Set<LucisSymbol> symbols;
        // TODO put current module if absent
        // this.currentModule = rootModule.foundModule(name);
    }

    public Optional<LucisVariable> findVariable(String name) {
        return Optional.ofNullable(variableMap.get(name)).or(() -> parent().flatMap(c -> c.findVariable(name)));
    }

    public void foundVariable(String name, LucisVariable variable) {
        if (variableMap.containsKey(name)) throw new SemanticException("variable " + name + " is already defined");
        variableMap.put(name, variable);
    }

    public Set<LucisModule> importedModules() {
        return importedModules;
    }
}
