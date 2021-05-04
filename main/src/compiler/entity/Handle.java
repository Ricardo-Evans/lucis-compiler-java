package compiler.entity;

public final class Handle {
    private final Object[] content;
    private final Position position;

    public Handle(Object[] content, Position position) {
        this.content = content;
        this.position = position;
    }

    public Position position() {
        return position;
    }

    @SuppressWarnings("unchecked")
    public <T> T at(int index) {
        return (T) content[index];
    }
}
