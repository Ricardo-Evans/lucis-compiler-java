package lucis.compiler.tokenizer;

import lucis.compiler.entity.SyntaxTree;

import java.io.IOException;

public interface TokenStream {
    SyntaxTree next();
}
