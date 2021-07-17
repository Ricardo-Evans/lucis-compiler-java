package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Collection;

public interface Analyzer<T extends SyntaxTree<T>> {
    void analyze(Collection<T> trees);

    interface Builder<T extends SyntaxTree<T>> {
        Analyzer<T> build();

        Builder<T> definePass(Pass<T> pass);
    }
}
