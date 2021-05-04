package lucis.compiler.syntax;

public class DoubleOperatorExpression extends OperatorExpression {
    public final Expression expression1, expression2;

    public DoubleOperatorExpression(Operator operator, Expression expression1, Expression expression2) {
        super(operator);
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitDoubleOperatorExpression(this);
    }
}
