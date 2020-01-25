package lucis.compiler.entity;

import java.util.HashMap;
import java.util.Map;

public class TypeTree implements SyntaxTree {
    private String name;
    private boolean isVirtual;
    private Map<String, FunctionTree> functions = new HashMap<>();
    private Map<String, VariableTree> variables = new HashMap<>();

    public TypeTree(String name, boolean isVirtual) {
        this.name = name;
        this.isVirtual = isVirtual;
    }

    public String getName() {
        return name;
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public Map<String, FunctionTree> getFunctions() {
        return functions;
    }

    public Map<String, VariableTree> getVariables() {
        return variables;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return null;
    }
}
