package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Collection;

public interface Analyzer<T extends SyntaxTree<T>, E> {
    void analyze(Collection<T> trees, E environment);

    interface Builder<T extends SyntaxTree<T>, E> {
        Analyzer<T, E> build();

        Builder<T, E> definePass(Pass<T, E> pass);
    }
}
