package lucis.compiler.syntax;

import java.util.List;

public class BlockExpression extends Expression {
    public final List<Statement> statements;

    public BlockExpression(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitBlockExpression(this, data);
    }
}
