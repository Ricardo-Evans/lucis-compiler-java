package lucis.compiler.semantic.concept;

public class ClassType extends BasicType {
    public ClassType(String module, String name) {
        super(module, name);
    }

    @Override
    public boolean match(LucisType type) {
        return false;
    }

    @Override
    public boolean assignable(LucisType type) {
        return false;
    }
}
