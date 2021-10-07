package lucis.compiler.semantic;

public interface LucisType extends LucisObject {
    String name();

    String module();

    String signature();

    boolean is(LucisType type);
}
