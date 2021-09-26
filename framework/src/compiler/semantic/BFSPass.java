package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class BFSPass<T extends SyntaxTree<T>, E> implements Pass<T, E> {
    private final List<Step<T, E>> steps;

    private BFSPass(List<Step<T, E>> steps) {
        this.steps = steps;
    }

    @Override
    public void process(T tree, Deque<T> deque, E environment) {
        steps.forEach(step -> step.process(tree, environment));
        tree.children().forEach(deque::offerLast);
    }

    public static class Builder<T extends SyntaxTree<T>, E> implements Pass.Builder<T, E> {
        private final List<Step<T, E>> steps = new LinkedList<>();

        public Builder() {
        }

        @Override
        public Pass<T, E> build() {
            return new BFSPass<>(steps);
        }

        @Override
        public Builder<T, E> step(Step<T, E> step) {
            steps.add(step);
            return this;
        }
    }
}
