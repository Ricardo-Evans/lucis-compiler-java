package compiler.entity;

public interface SyntaxTree<T extends SyntaxTree<T, C>, C> {
    Iterable<? extends T> children();

    Position startPosition();

    Position endPosition();

    void position(Position startPosition,Position endPosition);

    C context();
}
