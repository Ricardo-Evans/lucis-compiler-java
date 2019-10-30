package lucis.compiler.entity;

import java.io.Serializable;

public class Position implements Serializable {
    private static final long serialVersionUID = 8729840806537371432L;

    public final long error = -1;

    private final long line;
    private final long column;

    public Position(long line, long column) {
        this.line = line;
        this.column = column;
    }

    public long line() {
        return line;
    }

    public long column() {
        return column;
    }

    @Override
    public String toString() {
        return "line:" + line + ", column:" + column;
    }
}
