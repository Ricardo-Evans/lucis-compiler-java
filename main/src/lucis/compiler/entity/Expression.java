package lucis.compiler.entity;

@Name("expression")
public class Expression implements SyntaxTree {
    @Override
    public Position position() {
        return null;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitExpression(this, data);
    }
}
