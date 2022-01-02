package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.*;

public class BasicAnalyzer<T extends SyntaxTree<T, C>, C, E> implements Analyzer<T, C, E> {
    private final List<Pass<T, C, E>> passes;

    private BasicAnalyzer(List<Pass<T, C, E>> passes) {
        this.passes = passes;
    }

    @Override
    public void analyze(Collection<T> trees, E environment) {
        trees.forEach(tree -> passes.forEach(pass -> {
            pass.setup();
            pass.process(tree, environment);
            pass.clear();
        }));
    }

    public static class Builder<T extends SyntaxTree<T, C>, C, E> implements Analyzer.Builder<T, C, E> {
        private final List<Pass<T, C, E>> passes = new LinkedList<>();

        @Override
        public BasicAnalyzer<T, C, E> build() {
            return new BasicAnalyzer<>(passes);
        }

        @Override
        public Builder<T, C, E> definePass(Pass<T, C, E> pass) {
            passes.add(pass);
            return this;
        }
    }
}
