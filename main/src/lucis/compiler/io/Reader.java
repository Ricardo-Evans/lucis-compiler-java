package lucis.compiler.io;

import java.io.IOException;

/**
 * Used for read source files
 *
 * @author Ricardo Evans
 * @version 1.0
 */
public interface Reader {
    /**
     * Get the next character
     *
     * @return the next character
     * @throws IOException when underlying io fail
     */
    Character get() throws IOException;

    /**
     * Put the given character as the next character
     * The operation do not apply to the origin source, only work as a cache
     *
     * @param c the given character
     * @throws IOException when underlying io fail
     */
    void put(Character c) throws IOException;

    /**
     * Peek the next character without moving to next position
     *
     * @return The next character
     * @throws IOException when underlying io fail
     */
    default Character peek() throws IOException {
        Character c = get();
        if (c != null) put(c);
        return c;
    }

    /**
     * Skip the next character
     *
     * @return true if skip the next character successful, false otherwise, (e.g)reach the end of the file
     * @throws IOException when underlying io fail
     */
    boolean skip() throws IOException;

    /**
     * Get the current position in the origin source
     *
     * @return the position
     */
    long position();

    /**
     * Determine whether the reader can be read more
     *
     * @return whether the reader can be read more
     */
    boolean available();
}
