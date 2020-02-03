package lucis.compiler.parser;

import lucis.compiler.entity.FunctionDeclaration;
import lucis.compiler.entity.TypeDeclaration;
import lucis.compiler.entity.VariableDeclaration;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable implements lucis.compiler.entity.SymbolTable {
    private SymbolTable parent = null;
    private Map<String, TypeDeclaration> typeMap = new HashMap<>();
    private Map<String, VariableDeclaration> variableMap = new HashMap<>();
    private Map<String, FunctionDeclaration> functionMap = new HashMap<>();

    public SymbolTable() {
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    public TypeDeclaration getType(String name) {
        return typeMap.getOrDefault(name, parent == null ? null : parent.getType(name));
    }

    @Override
    public VariableDeclaration getVariable(String name) {
        return variableMap.getOrDefault(name, parent == null ? null : parent.getVariable(name));
    }

    @Override
    public FunctionDeclaration getFunction(String name) {
        return functionMap.getOrDefault(name, parent == null ? null : parent.getFunction(name));
    }

    @Override
    public void setType(String name, TypeDeclaration type) {
        typeMap.put(name, type);
    }

    @Override
    public void setVariable(String name, VariableDeclaration variable) {
        variableMap.put(name, variable);
    }

    @Override
    public void setFunction(String name, FunctionDeclaration function) {
        functionMap.put(name, function);
    }
}
