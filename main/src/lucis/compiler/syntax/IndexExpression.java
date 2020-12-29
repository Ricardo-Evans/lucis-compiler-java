package lucis.compiler.syntax;

public class IndexExpression extends Expression {
    public final Expression array;
    public final Expression index;

    public IndexExpression(Expression array, Expression index) {
        this.array = array;
        this.index = index;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitIndexExpression(this, data);
    }
}
