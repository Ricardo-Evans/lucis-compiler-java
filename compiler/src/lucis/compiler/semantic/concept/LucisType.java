package lucis.compiler.semantic.concept;

public interface LucisType extends NamedObject{
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
    boolean match(LucisType type);

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
    boolean assignable(LucisType type);
}
