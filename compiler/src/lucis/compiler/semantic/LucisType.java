package lucis.compiler.semantic;

public interface LucisType {
    String name();

    String signature();

    boolean is(LucisType type);
}
