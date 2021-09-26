package lucis.compiler.syntax;

public class LiteralExpression extends Expression {
    public enum Type {
        INTEGER,
        DECIMAL,
        STRING,
    }

    public final Type type;
    public final String value;

    public LiteralExpression(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitLiteralExpression(this);
    }
}
