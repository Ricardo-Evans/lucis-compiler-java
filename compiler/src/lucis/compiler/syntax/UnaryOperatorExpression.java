package lucis.compiler.syntax;

public class UnaryOperatorExpression extends OperatorExpression {
    public final Expression expression;

    public UnaryOperatorExpression(Operator operator, Expression expression) {
        super(operator, expression);
        this.expression = expression;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitUnaryOperatorExpression(this);
    }
}
