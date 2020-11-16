package lucis.compiler.entity;

public class Expression implements SyntaxTree {
    @Override
    public String name() {
        return "expression";
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitExpression(this, data);
    }
}
