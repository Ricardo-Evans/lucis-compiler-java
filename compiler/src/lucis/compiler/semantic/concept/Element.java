package lucis.compiler.semantic.concept;

import java.util.Optional;

public class Element {
    private final String name;
    private final LucisType type;
    private final boolean modifiable;
    private LucisObject value;

    public Element(String name, LucisType type, boolean modifiable) {
        this(name, type, modifiable, null);
    }

    public Element(String name, LucisType type, boolean modifiable, LucisObject value) {
        this.name = name;
        this.type = type;
        this.modifiable = modifiable;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public LucisType type() {
        return type;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public Optional<LucisObject> getValue() {
        return Optional.ofNullable(value);
    }

    public void setValue(LucisObject value) {
        this.value = value;
    }
}
