package lucis.compiler.syntax;

public class ReturnStatement extends Statement {
    public Expression value;

    public ReturnStatement(Expression value) {
        super(value);
        this.value = value;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitReturnStatement(this);
    }
}
