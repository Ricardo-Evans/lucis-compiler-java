package lucis.compiler.io;

import java.io.IOException;

/**
 * Used for read source files.
 *
 * @author Ricardo Evans
 * @version 1.0
 */
public interface Reader {
    /**
     * Get the next character.
     *
     * @return the next character
     * @throws IOException when underlying io fail
     */
    Integer next() throws IOException;

    /**
     * Peek the next character without moving to next position.
     *
     * @return The next character
     * @throws IOException when underlying io fail
     */
    Integer peek() throws IOException;

    /**
     * Skip the next character
     *
     * @return true if skip the next character successful, false otherwise, (e.g)reach the end of the file
     * @throws IOException when underlying io fail
     */
    boolean skip() throws IOException;

    /**
     * Determine whether the reader can be read more.
     *
     * @return whether the reader can be read more
     */
    boolean available();

    /**
     * Mark the reader at the current state, may use reset in future to recover the state.
     *
     * @see Reader#reset()
     */
    void mark();

    /**
     * Reset the reader to the marked state before, makes no effect if not marked.
     *
     * @see Reader#mark()
     */
    void reset();
}
