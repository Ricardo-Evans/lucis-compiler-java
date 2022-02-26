package lucis.compiler.semantic.concept;

import lucis.compiler.semantic.Utility;

import java.util.List;
import java.util.Objects;

public abstract class LucisType implements LucisObject {
    private static final LucisKind TypeKind = new LucisKind(Utility.LUCIS_CORE, "Type", List.of(new LucisKind.Parameter("type", Variant.INVARIANT)), StubType.fromName("type"));
    private final String signature;
    private final String name;

    protected LucisType(String name, String signature) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(signature);
        this.signature = signature;
        this.name = name;
    }

    public String name() {
        return name;
    }

    public String signature() {
        return signature;
    }

    /**
     * whether this type match the given type's constraint
     * <p>for operation whether T1 match T2, T1 should be regarded as a type while T2 as a constraint</p>
     * <p>
     * The difference between match and assignable can be explained by following example:<br/>
     * Suppose T is a simple type:<br/>
     * T match T+ (obviously)<br/>
     * T is not assignable to T+ (here T+ means an unknown subtype of T)<br/>
     * </p>
     */
    public abstract boolean match(LucisType type);

    /**
     * whether this type is assignable to the given type
     * <p>for operation whether T1 is assignable to T2, both T1 and T2 should be regarded as a type</p>
     * <p>
     * The difference between match and assignable can be explained by following example:<br/>
     * Suppose T is a simple type:<br/>
     * T match T+ (obviously)<br/>
     * T is not assignable to T+ (here T+ means an unknown subtype of T)<br/>
     * </p>
     */
    public abstract boolean assignable(LucisType type);

    @Override
    public String toString() {
        return signature();
    }
}
