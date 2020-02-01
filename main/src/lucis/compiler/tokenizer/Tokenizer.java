package lucis.compiler.tokenizer;

import lucis.compiler.entity.Tag;
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

    /**
     * Skip tokens until the next token's tag is not the specified one
     *
     * @param tag the specified tag to be skip
     */
    default Tokenizer skip(Tag tag) {
        while (peek().tag() == tag) next();
        return this;
    }
}
