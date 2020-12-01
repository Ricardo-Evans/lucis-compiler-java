package lucis.compiler.syntax;

public class SingleOperatorExpression extends OperatorExpression {
    public final Expression expression;

    public SingleOperatorExpression(Operator operator, Expression expression) {
        super(operator);
        this.expression = expression;
    }
}
