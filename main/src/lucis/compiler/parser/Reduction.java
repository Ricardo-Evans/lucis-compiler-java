package lucis.compiler.parser;

import lucis.compiler.entity.SyntaxTree;

@FunctionalInterface
public interface Reduction {
    SyntaxTree reduce(SyntaxTree... nodes);
}
