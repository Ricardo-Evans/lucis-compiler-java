package compiler.entity;

public interface SyntaxTree<T extends SyntaxTree<T, C>, C> {
    Iterable<? extends T> children();

    Position position();

    C context();
}
