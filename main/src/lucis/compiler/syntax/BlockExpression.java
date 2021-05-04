package lucis.compiler.syntax;

import java.util.List;

public class BlockExpression extends Expression {
    public final List<Statement> statements;

    public BlockExpression(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitBlockExpression(this);
    }
}
