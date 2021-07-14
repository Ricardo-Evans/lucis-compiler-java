package compiler.entity;

import java.util.List;

public interface SyntaxTree<T extends SyntaxTree<T>> {
    List<T> children();

    Position position();
}
