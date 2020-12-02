package lucis.compiler.syntax;

public class LiteralExpression {
    public enum Type{
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
}
