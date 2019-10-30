package lucis.compiler.entity;

import java.io.Serializable;

public final class Token implements Serializable {
    private static final long serialVersionUID = -3786791175193882275L;
    public static final Token END = new Token(null, Tag.END, null);
    private final Position position;
    private final Object value;
    private final Tag tag;

    public Token(Object value, Tag tag, Position position) {
        this.value = value;
        this.tag = tag;
        this.position = position;
    }

    public Object value() {
        return value;
    }

    public Tag tag() {
        return tag;
    }

    public Position position() {
        return position;
    }
}
