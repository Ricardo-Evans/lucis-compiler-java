package compiler.semantic;

import compiler.entity.SyntaxTree;

@FunctionalInterface
public interface Step<T extends SyntaxTree<T>, E> {
    boolean process(T tree, E environment);
}
