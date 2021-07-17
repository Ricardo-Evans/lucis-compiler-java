package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class DFSPass<T extends SyntaxTree<T>> implements Pass<T> {
    private final List<Step<T>> steps;

    private DFSPass(List<Step<T>> steps) {
        this.steps = steps;
    }

    @Override
    public void process(T tree, Deque<T> deque) {
        boolean flag = steps.stream().map(step -> step.process(tree)).reduce(false, (b1, b2) -> b1 | b2);
        if (flag) tree.children().forEach(deque::offerFirst);
    }

    public static class Builder<T extends SyntaxTree<T>> implements Pass.Builder<T> {
        private final List<Step<T>> steps = new LinkedList<>();

        @Override
        public Pass<T> build() {
            return new DFSPass<>(steps);
        }

        @Override
        public DFSPass.Builder<T> step(Step<T> step) {
            return this;
        }
    }
}
