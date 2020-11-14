package lucis.compiler.lexer;

import lucis.compiler.entity.Position;
import lucis.compiler.entity.SyntaxTree;

@FunctionalInterface
public interface LexicalRule {
    SyntaxTree apply(String content, Position position);
}
