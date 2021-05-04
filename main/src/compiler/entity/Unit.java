package compiler.entity;

public final class Unit {
    private final String name;
    private final Object value;
    private final Position position;

    public Unit(String name, Object value, Position position) {
        this.name = name;
        this.value = value;
        this.position = position;
    }

    public String name() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public <T> T value() {
        return (T) value;
    }

    public Position position() {
        return position;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", position=" + position +
                '}';
    }
}
