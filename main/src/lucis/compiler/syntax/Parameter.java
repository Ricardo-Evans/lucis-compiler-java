package lucis.compiler.syntax;

import lucis.compiler.entity.Position;

public class Parameter implements SyntaxTree {
    public final Expression type;
    public final String identifier;

    public Parameter(Expression type, String identifier) {
        this.type = type;
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
