package lucis.compiler.syntax;

public class NegationExpression implements Expression {
    public final Expression expression;

    public NegationExpression(Expression expression) {
        this.expression = expression;
    }
}
