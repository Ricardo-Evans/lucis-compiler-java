package lucis.compiler.semantic.concept;

import java.util.Objects;

public abstract class LucisType implements LucisObject {
    private final String module;
    private final String name;

    protected LucisType(String module, String name) {
        Objects.requireNonNull(module);
        Objects.requireNonNull(name);
        this.module = module;
        this.name = name;
    }

    public String name() {
        return name;
    }

    public String module() {
        return module;
    }

    public String signature() {
        return module + ':' + name;
    }

    public abstract boolean is(LucisType type);
}
