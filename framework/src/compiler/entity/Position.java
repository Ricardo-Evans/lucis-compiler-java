package compiler.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * The combination of line and offset
 *
 * @author Ricardo Evans
 * @version 1.0
 */
public record Position(long line, long offset) implements Serializable {
    @Serial
    private static final long serialVersionUID = 8729840806537371432L;
    public static final Position ROOT = new Position(1, 1);

    @Override
    public String toString() {
        return line + ":" + offset;
    }
}
