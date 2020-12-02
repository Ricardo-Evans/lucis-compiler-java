package lucis.compiler.syntax;

public class AssignStatement implements SyntaxTree {
    public final String identifier;
    public final Expression content;

    public AssignStatement(String identifier, Expression content) {
        this.identifier = identifier;
        this.content = content;
    }
}
