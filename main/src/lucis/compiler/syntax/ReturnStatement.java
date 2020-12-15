package lucis.compiler.syntax;

public class ReturnStatement extends Statement {
    public Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitReturnStatement(this, data);
    }
}
