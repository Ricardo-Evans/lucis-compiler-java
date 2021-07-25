package lucis.compiler.syntax;

public class AssignStatement extends Statement {
    public final Expression left;
    public final Expression right;

    public AssignStatement(Expression left, Expression right) {
        super(left, right);
        this.left = left;
        this.right = right;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitAssignStatement(this);
    }
}
