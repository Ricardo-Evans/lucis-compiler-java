package lucis.compiler.syntax;

public class DiscardStatement extends Statement {
    public final Expression expression;

    public DiscardStatement(Expression expression) {
        super(expression);
        this.expression = expression;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitDiscardStatement(this);
    }
}
