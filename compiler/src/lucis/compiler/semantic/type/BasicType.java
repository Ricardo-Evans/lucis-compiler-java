package lucis.compiler.semantic.type;

import lucis.compiler.semantic.LucisType;

import java.util.Objects;

public abstract class BasicType implements LucisType {
    private final String module;
    private final String name;

    protected BasicType(String module, String name) {
        Objects.requireNonNull(module);
        Objects.requireNonNull(name);
        this.module = module;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String module() {
        return module;
    }

    @Override
    public String signature() {
        return module + ':' + name;
    }
}
