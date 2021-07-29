package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import lucis.compiler.syntax.Symbol;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class LucisModule implements Serializable {
    @Serial
    private static final long serialVersionUID = 8968825529218018034L;
    public final String name;
    public final LucisModule parent;
    private final Map<String, LucisModule> modules = new HashMap<>();
    private final Map<String, LucisType> types = new HashMap<>();
    private final Map<String, LucisFunction> functions = new HashMap<>();
    private final Map<LucisObject, Integer> constants = new HashMap<>();
    private final Map<String, LucisVariable> variables = new HashMap<>();

    public LucisModule(String name) {
        this(name, null);
    }

    public LucisModule(String name, LucisModule parent) {
        this.name = name;
        this.parent = parent;
    }

    public Optional<LucisModule> findModule(Symbol symbol) {
        if (symbol == null) return Optional.of(this);
        return Optional.ofNullable(modules.get(symbol.name())).flatMap(m -> m.findModule(symbol.child()));
    }

    public LucisModule foundModule(Symbol symbol) {
        if (symbol == null) return this;
        modules.putIfAbsent(symbol.name(), new LucisModule(symbol.name(), this));
        return modules.get(symbol.name()).foundModule(symbol.child());
    }

    public Optional<LucisType> findType(Symbol symbol) {
        Objects.requireNonNull(symbol);
        if (symbol.child() == null) return Optional.ofNullable(types.get(symbol.name()));
        return Optional.ofNullable(modules.get(symbol.name())).flatMap(m -> m.findType(symbol.child()));
    }

    public void foundType(String name, LucisType type) {
        Objects.requireNonNull(name);
        if (types.containsKey(name)) throw new SemanticException("type: " + name + " already defined");
        types.put(name, type);
    }
}
