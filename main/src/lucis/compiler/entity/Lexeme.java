package lucis.compiler.entity;

import java.io.Serializable;

public final class Lexeme implements Serializable, SyntaxTree {
    private static final long serialVersionUID = -3786791175193882275L;
    private final String name;
    private final String content;
    private final Position position;

    public Lexeme(String name, String content, Position position) {
        this.name = name;
        this.content = content;
        this.position = position;
    }

    @Override
    public String name() {
        return name;
    }

    public String content() {
        return content;
    }

    public Position position() {
        return position;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitLexeme(this, data);
    }

    @Override
    public String toString() {
        return "Lexeme{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", position=" + position +
                '}';
    }
}
