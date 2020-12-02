package lucis.compiler.entity;

public class Array {
    private final Object[] content;

    public Array(Object[] content) {
        this.content = content;
    }

    @SuppressWarnings("unchecked")
    public <T> T at(int index) {
        return (T) content[index];
    }
}
