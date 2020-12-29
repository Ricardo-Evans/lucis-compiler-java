package lucis.compiler.syntax;

public class SingleOperatorExpression extends OperatorExpression {
    public final Expression expression;

    public SingleOperatorExpression(Operator operator, Expression expression) {
        super(operator);
        this.expression = expression;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitSingleOperatorExpression(this, data);
    }
}
