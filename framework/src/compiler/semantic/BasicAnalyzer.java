package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.*;

public class BasicAnalyzer<T extends SyntaxTree<T>, E> implements Analyzer<T, E> {
    private final List<Pass<T, E>> passes;

    private BasicAnalyzer(List<Pass<T, E>> passes) {
        this.passes = passes;
    }

    @Override
    public void analyze(Collection<T> trees, E environment) {
        passes.forEach(pass -> {
            pass.setup();
            Deque<T> queue = new LinkedList<>(trees);
            while (!queue.isEmpty()) {
                T t = queue.poll();
                pass.process(t, queue, environment);
            }
            pass.clear();
        });
    }

    public static class Builder<T extends SyntaxTree<T>, E> implements Analyzer.Builder<T, E> {
        private final List<Pass<T, E>> passes = new LinkedList<>();

        @Override
        public BasicAnalyzer<T, E> build() {
            return new BasicAnalyzer<>(passes);
        }

        @Override
        public Builder<T, E> definePass(Pass<T, E> pass) {
            passes.add(pass);
            return this;
        }
    }
}
