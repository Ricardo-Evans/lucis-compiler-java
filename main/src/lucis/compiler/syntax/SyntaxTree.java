package lucis.compiler.syntax;

import lucis.compiler.entity.Position;

public abstract class SyntaxTree {
    private Position position;

    protected SyntaxTree() {
    }

    public Position position() {
        return position;
    }

    public SyntaxTree position(Position position) {
        this.position = position;
        return this;
    }

    // public abstract <R, D> R visit(Visitor<R, D> visitor, D data);

    public interface Visitor<R, D> {
        R visitSource(Source source, D data);
    }
}
