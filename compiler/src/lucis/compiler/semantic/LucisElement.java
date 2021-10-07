package lucis.compiler.semantic;

public interface LucisElement {
    String name();

    String signature();

    LucisType type();
}
