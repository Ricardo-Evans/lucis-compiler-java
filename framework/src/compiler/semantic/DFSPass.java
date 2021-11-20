package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class DFSPass<T extends SyntaxTree<T>, E> implements Pass<T, E> {
    private final List<Step<T, E>> steps;

    private DFSPass(List<Step<T, E>> steps) {
        this.steps = steps;
    }

    @Override
    public void process(T tree, E environment) {
        Deque<T> deque = new LinkedList<>();
        deque.offer(tree);
        while (!deque.isEmpty()) {
            T t = deque.poll();
            try {
                steps.forEach(step -> step.process(t, environment));
            } catch (Exception e) {
                throw new SemanticException("semantic analyze fail at " + t.position(), e);
            }
            t.children().forEach(deque::offerFirst);
        }
    }

    public static class Builder<T extends SyntaxTree<T>, E> implements Pass.Builder<T, E> {
        private final List<Step<T, E>> steps = new LinkedList<>();

        @Override
        public Pass<T, E> build() {
            return new DFSPass<>(steps);
        }

        @Override
        public DFSPass.Builder<T, E> step(Step<T, E> step) {
            steps.add(step);
            return this;
        }
    }
}
