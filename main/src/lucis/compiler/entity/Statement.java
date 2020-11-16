package lucis.compiler.entity;

public class Statement implements SyntaxTree {
    @Override
    public String name() {
        return "statement";
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitStatement(this, data);
    }
}
