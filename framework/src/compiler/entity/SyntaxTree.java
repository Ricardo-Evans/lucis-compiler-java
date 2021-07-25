package compiler.entity;

import java.util.List;

public interface SyntaxTree<T extends SyntaxTree<T>> {
    List<? extends T> children();

    Position position();
}
