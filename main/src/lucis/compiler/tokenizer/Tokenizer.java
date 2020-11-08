package lucis.compiler.tokenizer;

import lucis.compiler.entity.SyntaxTree;

@FunctionalInterface
public interface Tokenizer {
    SyntaxTree tokenize(String content);
}
