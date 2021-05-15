package lucis.compiler.syntax;

import java.util.List;

public class BlockStatement extends Statement {
    public final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitBlockStatement(this);
    }
}
