package lucis.compiler.syntax;

public class BranchStatement extends Statement {
    public final Expression condition;
    public final Statement positive, negative;

    public BranchStatement(Expression condition, Statement positive, Statement negative) {
        this.condition = condition;
        this.positive = positive;
        this.negative = negative;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitBranchStatement(this, data);
    }
}
