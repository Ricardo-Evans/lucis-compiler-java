package lucis.compiler.syntax;

public class IdentifierExpression extends Expression {
    public final Expression parent;
    public final String identifier;

    public IdentifierExpression(IdentifierExpression parent, String identifier) {
        this.parent = parent;
        this.identifier = identifier;
    }

    public IdentifierExpression(String identifier) {
        this(null, identifier);
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitIdentifierExpression(this, data);
    }
}
