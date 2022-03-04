package lucis.compiler.semantic.concept;

public interface NamedObject extends LucisObject {
    String name();

    String module();

    String signature();
}
