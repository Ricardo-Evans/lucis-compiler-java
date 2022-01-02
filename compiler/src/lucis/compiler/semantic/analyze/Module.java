package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.concept.LucisObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Module implements Serializable {
    @Serial
    private static final long serialVersionUID = 8968825529218018034L;
    private final String name;
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final Map<LucisObject, Integer> constants = new HashMap<>();

    public Module(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Map<String, Symbol> symbols() {
        return symbols;
    }

    public Symbol findSymbol(String name) {
        if (!symbols.containsKey(name))
            throw new SemanticException("symbol of name " + name + " does not exist in " + this);
        return symbols.get(name);
    }

    public void foundSymbol(String name) {
        Objects.requireNonNull(name);
        symbols.putIfAbsent(name, new Symbol(name));
    }
}
