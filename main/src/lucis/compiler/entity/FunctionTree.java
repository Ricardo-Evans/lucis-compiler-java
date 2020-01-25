package lucis.compiler.entity;

import java.util.LinkedList;
import java.util.List;

public class FunctionTree implements SyntaxTree {
    private String name;
    private boolean isNative;
    private boolean isVirtual;
    private List<VariableTree> parameters = new LinkedList<>();
    private List<StatementTree> statements = null;

    public FunctionTree(String name, boolean isNative, boolean isVirtual) {
        this.name = name;
        this.isNative = isNative;
        this.isVirtual = isVirtual;
        if (!isNative && !isVirtual) statements = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isNative() {
        return isNative;
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public List<VariableTree> getParameters() {
        return parameters;
    }

    public List<StatementTree> getStatements() {
        return statements;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return null;
    }
}
