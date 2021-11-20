package lucis.compiler.semantic.concept;

public class TraitType extends LucisType {
    public TraitType(String module, String name) {
        super(module, name);
    }

    @Override
    public boolean is(LucisType type) {
        return false;
    }
}
