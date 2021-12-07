package compiler.semantic;

import compiler.entity.SyntaxTree;

@FunctionalInterface
public interface Step<T extends SyntaxTree<T, C>, C, E> {
    void process(T tree, C context, E environment);
}
