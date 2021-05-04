package lucis.compiler.syntax;

public class ExpressionStatement extends Statement {
    public final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitExpressionStatement(this);
    }
}
