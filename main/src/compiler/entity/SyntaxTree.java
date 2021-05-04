package compiler.entity;

public interface SyntaxTree<C, V> {
    C context();

    SyntaxTree<C, V>[] children();

    void visit(V visitor);

    Position position();
}
