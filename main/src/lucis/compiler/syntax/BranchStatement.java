package lucis.compiler.syntax;

public class BranchStatement implements Statement {
    public final Expression condition;
    public final Statement positive, negative;

    public BranchStatement(Expression condition, Statement positive, Statement negative) {
        this.condition = condition;
        this.positive = positive;
        this.negative = negative;
    }
}
