package lucis.compiler.semantic.concept;

public interface Element {
    String name();

    String signature();

    LucisType type();
}
