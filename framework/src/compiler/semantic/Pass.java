package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Deque;

@FunctionalInterface
public interface Pass<T extends SyntaxTree<T, ?>, E> {
    default void setup() {
    }

    default void clear() {
    }

    void process(T tree, E environment);
}
