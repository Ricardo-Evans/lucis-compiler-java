package lucis.compiler.syntax;

public class IndexExpression implements Expression {
    public final Expression array;
    public final Expression index;

    public IndexExpression(Expression array, Expression index) {
        this.array = array;
        this.index = index;
    }
}
