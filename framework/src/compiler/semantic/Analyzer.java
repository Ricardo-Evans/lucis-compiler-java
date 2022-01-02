package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Collection;

public interface Analyzer<T extends SyntaxTree<T, C>, C, E> {
    void analyze(Collection<T> trees, E environment);

    interface Builder<T extends SyntaxTree<T, C>, C, E> {
        Analyzer<T, C, E> build();

        Builder<T, C, E> definePass(Pass<T, C, E> pass);
    }
}
