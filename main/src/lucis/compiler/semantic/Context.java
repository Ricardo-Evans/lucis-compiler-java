package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import lucis.compiler.ir.LucisType;
import lucis.compiler.ir.LucisVariable;

import java.util.*;

public class Context {
    private String module;
    private final Context parent;
    private final Map<String, LucisType> typeMap = new HashMap<>();
    private final Map<String, LucisVariable> variableMap = new HashMap<>();
    private final Set<String> importedModules = new HashSet<>();

    public Context() {
        this(null);
    }

    public Context(Context parent) {
        this.parent = parent;
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

    public Optional<String> findModule() {
        return Optional.ofNullable(module).or(parent::findModule);
    }

    public void foundModule(String name) {
        if (this.module != null)
            throw new SemanticException("cannot define module " + name + " in module " + this.module);
        this.module = name;
    }

    public Optional<LucisVariable> findVariable(String name) {
        return Optional.ofNullable(variableMap.get(name)).or(() -> parent().flatMap(c -> c.findVariable(name)));
    }

    public void foundVariable(String name, LucisVariable variable) {
        if (variableMap.containsKey(name)) throw new SemanticException("variable " + name + " is already defined");
        variableMap.put(name, variable);
    }

    public Set<String> importedModules() {
        return importedModules;
    }

    public void importModule(String name) {
        importedModules.add(name);
    }
}
