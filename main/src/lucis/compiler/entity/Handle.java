package lucis.compiler.entity;

public class Handle {
    private final Object[] content;

    public Handle(Object[] content) {
        this.content = content;
    }

    @SuppressWarnings("unchecked")
    public <T> T at(int index) {
        return (T) content[index];
    }
}
