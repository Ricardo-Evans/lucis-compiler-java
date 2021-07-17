package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.*;

public class BasicAnalyzer<T extends SyntaxTree<T>> implements Analyzer<T> {
    private final List<Pass<T>> passes;

    private BasicAnalyzer(List<Pass<T>> passes) {
        this.passes = passes;
    }

    @Override
    public void analyze(Collection<T> trees) {
        passes.forEach(pass -> {
            pass.setup();
            Deque<T> queue = new LinkedList<>(trees);
            while (!queue.isEmpty()) {
                T t = queue.poll();
                pass.process(t, queue);
            }
            pass.clear();
        });
    }

    public static class Builder<T extends SyntaxTree<T>> implements Analyzer.Builder<T> {
        private final List<Pass<T>> passes = new LinkedList<>();

        @Override
        public BasicAnalyzer<T> build() {
            return new BasicAnalyzer<>(passes);
        }

        @Override
        public Builder<T> definePass(Pass<T> pass) {
            passes.add(pass);
            return this;
        }
    }
}
