package lucis.compiler.syntax;

public class ElementExpression extends Expression {
    public final Expression parent;
    public final String identifier;

    public ElementExpression(Expression parent, String identifier) {
        super(parent);
        this.parent = parent;
        this.identifier = identifier;
    }

    public ElementExpression(String identifier) {
        this(null, identifier);
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitIdentifierExpression(this);
    }
}
