package lucis.compiler.entity;

import java.io.Serializable;

/**
 * The combination of line and column
 *
 * @author Ricardo Evans
 * @version 1.0
 */
public class Position implements Serializable {
    private static final long serialVersionUID = 8729840806537371432L;

    public final long error = -1;

    private final long line;
    private final long column;

    /**
     * Construct a position of the given line and column
     *
     * @param line   the given line
     * @param column the given column
     */
    public Position(long line, long column) {
        this.line = line;
        this.column = column;
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
     * Get the column
     *
     * @return the column
     */
    public long column() {
        return column;
    }

    @Override
    public String toString() {
        return "line:" + line + ", column:" + column;
    }
}
