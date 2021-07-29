package compiler.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * The combination of line and offset
 *
 * @author Ricardo Evans
 * @version 1.0
 */
public final class Position implements Serializable {
    @Serial
    private static final long serialVersionUID = 8729840806537371432L;

    public static final Position ROOT = new Position(1, 1);

    private final long line;
    private final long offset;

    /**
     * Construct a position of the given line and offset
     *
     * @param line   the given line
     * @param offset the given offset
     */
    public Position(long line, long offset) {
        this.line = line;
        this.offset = offset;
    }

    /**
     * Get the line
     *
     * @return the line
     */
    public long line() {
        return line;
    }

    /**
     * Get the offset
     *
     * @return the offset
     */
    public long offset() {
        return offset;
    }

    @Override
    public String toString() {
        return line + ":" + offset;
    }
}
