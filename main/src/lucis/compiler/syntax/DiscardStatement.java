package lucis.compiler.syntax;

public class DiscardStatement implements Statement {
    public final Expression expression;

    public DiscardStatement(Expression expression) {
        this.expression = expression;
    }
}
