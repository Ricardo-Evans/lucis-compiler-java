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
     * Peek the ith token, where i is 0-based
     * This operation will not change the position of the tokenizer
     *
     * @param i the given index
     * @return the ith token
     */
    Token peek(int i);

    /**
     * Peek the next token, same as peek(0)
     *
     * @return the next token
     */
    default Token peek() {
        return peek(0);
    }

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
