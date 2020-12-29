package lucis.compiler.syntax;

public class ReturnStatement extends Statement {
    public Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitReturnStatement(this, data);
    }
}
