package lucis.compiler.syntax;

import lucis.compiler.entity.Position;

public interface SyntaxTree {
    Position position();

    <R, D> R visit(Visitor<R, D> visitor, D data);

    interface Visitor<R, D> {
        R visitSource(Source source, D data);
    }
}
