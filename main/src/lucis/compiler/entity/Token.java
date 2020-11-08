package lucis.compiler.entity;

import java.io.Serializable;

public final class Token implements Serializable, SyntaxTree {
    private static final long serialVersionUID = -3786791175193882275L;
    private final Position position;
    private final Object value;
    private final String tag;

    public Token(Object value, String tag, Position position) {
        this.value = value;
        this.tag = tag;
        this.position = position;
    }

    public Object value() {
        return value;
    }

    @Override
    public String tag() {
        return tag;
    }

    public Position position() {
        return position;
    }
}
