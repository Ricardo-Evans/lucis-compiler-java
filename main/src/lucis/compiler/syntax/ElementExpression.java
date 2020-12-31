package lucis.compiler.syntax;

public class ElementExpression extends Expression {
    public final Expression parent;
    public final String identifier;

    public ElementExpression(Expression parent, String identifier) {
        this.parent = parent;
        this.identifier = identifier;
    }

    public ElementExpression(String identifier) {
        this(null, identifier);
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitIdentifierExpression(this, data);
    }
}
