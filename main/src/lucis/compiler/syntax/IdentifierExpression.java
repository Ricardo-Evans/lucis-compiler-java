package lucis.compiler.syntax;

public class IdentifierExpression implements Expression {
    public final Expression parent;
    public final String identifier;

    public IdentifierExpression(IdentifierExpression parent, String identifier) {
        this.parent = parent;
        this.identifier = identifier;
    }

    public IdentifierExpression(String identifier) {
        this(null, identifier);
    }
}
