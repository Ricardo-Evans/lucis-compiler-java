package lucis.compiler.syntax;

public class AssignStatement extends Statement {
    public final String identifier;
    public final Expression content;

    public AssignStatement(String identifier, Expression content) {
        this.identifier = identifier;
        this.content = content;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitAssignStatement(this);
    }
}
