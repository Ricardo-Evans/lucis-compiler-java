package lucis.compiler.parser;

import lucis.compiler.entity.*;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable implements lucis.compiler.entity.SymbolTable {
    private SymbolTable parent = null;
    private Map<String, Type> typeMap = new HashMap<>();
    private Map<String, Variable> variableMap = new HashMap<>();
    private Map<String, Function> functionMap = new HashMap<>();

    public SymbolTable() {
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    public Type getType(String name) {
        return typeMap.getOrDefault(name, parent == null ? null : parent.getType(name));
    }

    @Override
    public Variable getVariable(String name) {
        return variableMap.getOrDefault(name, parent == null ? null : parent.getVariable(name));
    }

    @Override
    public Function getFunction(String name) {
        return functionMap.getOrDefault(name, parent == null ? null : parent.getFunction(name));
    }

    @Override
    public void setType(String name, Type type) {
        typeMap.put(name, type);
    }

    @Override
    public void setVariable(String name, Variable variable) {
        variableMap.put(name, variable);
    }

    @Override
    public void setFunction(String name, Function function) {
        functionMap.put(name, function);
    }
}
