package lucis.compiler.syntax;

public class BranchStatement extends Statement {
    public final Expression condition;
    public final Statement positive, negative;

    public BranchStatement(Expression condition, Statement positive, Statement negative) {
        super(condition, positive, negative);
        this.condition = condition;
        this.positive = positive;
        this.negative = negative;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitBranchStatement(this);
    }
}
