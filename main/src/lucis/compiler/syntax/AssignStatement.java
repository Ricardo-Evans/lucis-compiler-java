package lucis.compiler.syntax;

public class AssignStatement implements Statement{
    public final String identifier;
    public final Expression content;

    public AssignStatement(String identifier, Expression content) {
        this.identifier = identifier;
        this.content = content;
    }
}
