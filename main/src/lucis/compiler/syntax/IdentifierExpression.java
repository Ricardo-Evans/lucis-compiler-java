package lucis.compiler.syntax;

import lucis.compiler.entity.Position;

public class IdentifierExpression implements Expression {
    public final String identifier;

    public IdentifierExpression(String identifier) {
        this.identifier = identifier;
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
