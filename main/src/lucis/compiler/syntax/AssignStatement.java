package lucis.compiler.syntax;

public class AssignStatement extends Statement {
    public final String identifier;
    public final Expression content;

    public AssignStatement(String identifier, Expression content) {
        this.identifier = identifier;
        this.content = content;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitAssignStatement(this, data);
    }
}
