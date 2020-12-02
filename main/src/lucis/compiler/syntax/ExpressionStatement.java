package lucis.compiler.syntax;

public class ExpressionStatement implements SyntaxTree {
    public final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }
}
