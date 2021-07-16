package lucis.compiler.syntax;

public class SingleOperatorExpression extends OperatorExpression {
    public final Expression expression;

    public SingleOperatorExpression(Operator operator, Expression expression) {
        super(operator, expression);
        this.expression = expression;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitSingleOperatorExpression(this);
    }
}
