package lucis.compiler.syntax;

public class DiscardStatement implements SyntaxTree {
    public final Expression expression;

    public DiscardStatement(Expression expression) {
        this.expression = expression;
    }
}