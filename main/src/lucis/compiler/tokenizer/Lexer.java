package lucis.compiler.tokenizer;

import lucis.compiler.io.Reader;

@FunctionalInterface
public interface Lexer {
    /**
     * Get the next token
     *
     * @return the next token
     */
    TokenStream resolve(Reader reader);
}
