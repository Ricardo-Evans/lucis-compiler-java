package lucis.compiler.syntax;

public class ExpressionStatement extends Statement {
    public final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitExpressionStatement(this, data);
    }
}
