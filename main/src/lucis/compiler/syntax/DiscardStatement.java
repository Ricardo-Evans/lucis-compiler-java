package lucis.compiler.syntax;

public class DiscardStatement extends Statement {
    public final Expression expression;

    public DiscardStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitDiscardStatement(this, data);
    }
}
