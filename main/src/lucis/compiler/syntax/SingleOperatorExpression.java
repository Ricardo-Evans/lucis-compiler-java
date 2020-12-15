package lucis.compiler.syntax;

public class SingleOperatorExpression extends OperatorExpression {
    public final Expression expression;

    public SingleOperatorExpression(Operator operator, Expression expression) {
        super(operator);
        this.expression = expression;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitSingleOperatorExpression(this, data);
    }
}
