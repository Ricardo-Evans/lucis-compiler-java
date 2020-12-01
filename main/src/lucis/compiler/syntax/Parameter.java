package lucis.compiler.syntax;

public class Parameter implements SyntaxTree {
    public final Expression type;
    public final String identifier;

    public Parameter(Expression type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }
}
