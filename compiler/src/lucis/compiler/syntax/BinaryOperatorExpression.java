package lucis.compiler.syntax;

public class BinaryOperatorExpression extends OperatorExpression {
    public final Expression expression1, expression2;

    public BinaryOperatorExpression(Operator operator, Expression expression1, Expression expression2) {
        super(operator, expression1, expression2);
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitBinaryOperatorExpression(this);
    }
}
