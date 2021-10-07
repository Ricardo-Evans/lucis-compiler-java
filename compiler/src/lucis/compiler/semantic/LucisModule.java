package lucis.compiler.semantic;

import compiler.semantic.SemanticException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class LucisModule implements Serializable {
    @Serial
    private static final long serialVersionUID = 8968825529218018034L;
    private final String name;
    private final Map<String, LucisSymbol> symbols = new HashMap<>();
    private final Map<LucisObject, Integer> constants = new HashMap<>();

    public LucisModule(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Map<String, LucisSymbol> symbols() {
        return symbols;
    }

    public LucisSymbol findSymbol(String name) {
        if (!symbols.containsKey(name))
            throw new SemanticException("symbol of name " + name + " does not exist in " + this);
        return symbols.get(name);
    }

    public void foundSymbol(String name) {
        Objects.requireNonNull(name);
        symbols.putIfAbsent(name, new LucisSymbol(name,this.name));
    }
}
