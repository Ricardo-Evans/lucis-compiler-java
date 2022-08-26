package lucis.compiler.semantic.concept;

import lucis.compiler.semantic.Utility;
import lucis.compiler.utility.Constants;

import java.util.List;
import java.util.Objects;

public abstract class BasicType implements LucisType {
    public static final LucisKind TypeKind = new LucisKind(Utility.LUCIS_CORE, "Type", List.of(new LucisKind.Parameter("type", Variant.INVARIANT)), StubType.fromName("type"));
    private final String module;
    private final String name;
    private final String signature;

    protected BasicType(String module, String name) {
        this(module, name, Utility.signature(module, name));
    }

    public BasicType(String module, String name, String signature) {
        Objects.requireNonNull(module);
        Objects.requireNonNull(name);
        Objects.requireNonNull(signature);
        this.module = module;
        this.name = name;
        this.signature = signature;
    }

    @Override
    public String module() {
        return module;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String signature() {
        return signature;
    }

    @Override
    public String toString() {
        return signature();
    }
}
