package compiler.semantic;

import compiler.entity.SyntaxTree;

@FunctionalInterface
public interface Step<T extends SyntaxTree<T>> {
    boolean process(T tree);
}
