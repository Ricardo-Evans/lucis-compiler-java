package lucis.compiler.syntax;

public class AssignStatement extends Statement {
    public final String identifier;
    public final Expression content;

    public AssignStatement(String identifier, Expression content) {
        super(content);
        this.identifier = identifier;
        this.content = content;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitAssignStatement(this);
    }
}
