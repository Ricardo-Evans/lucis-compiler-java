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
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitBranchStatement(this, data);
    }
}
