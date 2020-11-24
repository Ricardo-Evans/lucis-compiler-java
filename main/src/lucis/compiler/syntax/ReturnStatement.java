package lucis.compiler.syntax;

import lucis.compiler.entity.Position;

public class ReturnStatement implements SyntaxTree {
    public Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
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
