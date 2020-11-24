package lucis.compiler.syntax;

import lucis.compiler.entity.Position;

import java.util.List;

public class BlockExpression implements Expression {
    public final List<Statement> statements;
    public final Statement result;

    public BlockExpression(List<Statement> statements, Statement result) {
        this.statements = statements;
        this.result = result;
    }

    @Override
    public Position position() {
        return null;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return null;
    }
}
