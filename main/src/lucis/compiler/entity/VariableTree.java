package lucis.compiler.entity;

public class VariableTree implements SyntaxTree {
    private TypeTree type;
    private String name;

    public VariableTree(TypeTree type, String name) {
        this.type = type;
        this.name = name;
    }

    public TypeTree getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(TypeTree type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return null;
    }
}
