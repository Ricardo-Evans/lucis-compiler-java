package compiler.semantic;

import compiler.entity.SyntaxTree;

@FunctionalInterface
public interface Step<T extends SyntaxTree<T>, E> {
    void process(T tree, E environment);
}
