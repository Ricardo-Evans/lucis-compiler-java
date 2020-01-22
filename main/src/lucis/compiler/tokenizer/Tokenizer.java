package lucis.compiler.tokenizer;

import lucis.compiler.entity.Token;

public interface Tokenizer {
    /**
     * Get the next token
     *
     * @return the next token
     */
    Token next();

    /**
     * Get the next token, but keep the position
     *
     * @return the next token
     */
    Token peek();
}
