package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Deque;

@FunctionalInterface
public interface Pass<T extends SyntaxTree<T>> {
    default void setup() {
    }

    default void clear() {
    }

    void process(T tree, Deque<T> deque);

    interface Builder<T extends SyntaxTree<T>> {
        Pass<T> build();

        Builder<T> step(Step<T> step);
    }
}
