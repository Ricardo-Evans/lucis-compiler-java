package lucis.compiler.syntax;

public class UnaryOperatorExpression extends OperatorExpression {
    public final Expression expression;

    public UnaryOperatorExpression(Operator operator, Expression expression) {
        super(operator, expression);
        this.expression = expression;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitUnaryOperatorExpression(this);
    }
}
