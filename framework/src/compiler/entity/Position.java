package compiler.entity;

import java.io.Serial;
import java.io.Serializable;

/**
 * The combination of line, column and raw offset of file
 *
 * @author Ricardo Evans
 * @version 1.0
 */
public record Position(int line, int column, int offset) implements Serializable {
    @Serial
    private static final long serialVersionUID = 8729840806537371432L;

    @Override
    public String toString() {
        return line + ":" + column;
    }
}
