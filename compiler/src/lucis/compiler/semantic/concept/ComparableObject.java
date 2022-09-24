package lucis.compiler.semantic.concept;

public interface ComparableObject<T extends ComparableObject<T>> extends LucisObject {
    boolean equal(T object);

    boolean greater(T object);

    boolean greaterEqual(T object);

    boolean less(T object);

    boolean lessEqual(T object);
}
