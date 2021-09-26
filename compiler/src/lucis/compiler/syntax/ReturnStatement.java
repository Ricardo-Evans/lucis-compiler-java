package lucis.compiler.syntax;

public class ReturnStatement extends Statement {
    public Expression value;

    public ReturnStatement(Expression value) {
        super(value);
        this.value = value;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitReturnStatement(this);
    }
}
