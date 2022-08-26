package lucis.compiler.semantic.concept;

public interface LucisFunction extends NamedObject {
    LucisType parameterType();

    LucisType resultType();

    int stackSize();

    boolean isDynamic();

    boolean isOverride();

    boolean isNative();

    boolean isAbstract();
}
