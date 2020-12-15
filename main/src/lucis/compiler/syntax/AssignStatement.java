package lucis.compiler.syntax;

public class AssignStatement extends Statement {
    public final String identifier;
    public final Expression content;

    public AssignStatement(String identifier, Expression content) {
        this.identifier = identifier;
        this.content = content;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitAssignStatement(this, data);
    }
}
