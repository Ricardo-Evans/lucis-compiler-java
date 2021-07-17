package lucis.compiler.syntax;

import java.util.List;

public class BlockStatement extends Statement {
    public final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        super(statements.toArray(Statement[]::new));
        this.statements = statements;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitBlockStatement(this);
    }
}
