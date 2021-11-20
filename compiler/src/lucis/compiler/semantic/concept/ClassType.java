package lucis.compiler.semantic.concept;

public class ClassType extends LucisType {
    public ClassType(String module, String name) {
        super(module, name);
    }

    @Override
    public boolean is(LucisType type) {
        return false;
    }
}
