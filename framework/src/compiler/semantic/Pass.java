package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Deque;

@FunctionalInterface
public interface Pass<T extends SyntaxTree<T>, E> {
    default void setup() {
    }

    default void clear() {
    }

    void process(T tree, Deque<T> deque, E environment);

    interface Builder<T extends SyntaxTree<T>, E> {
        Pass<T, E> build();

        Builder<T, E> step(Step<T, E> step);
    }
}
