package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Deque;

@FunctionalInterface
public interface Pass<T extends SyntaxTree<T, C>, C, E> {
    default void setup() {
    }

    default void clear() {
    }

    void process(T tree, E environment);

    interface Builder<T extends SyntaxTree<T, C>, C, E> {
        Pass<T, C, E> build();

        Builder<T, C, E> step(Step<T, C, E> step);
    }
}
