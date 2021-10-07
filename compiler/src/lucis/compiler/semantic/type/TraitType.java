package lucis.compiler.semantic.type;

import lucis.compiler.semantic.LucisType;

public class TraitType extends BasicType {
    public TraitType(String module, String name) {
        super(module, name);
    }

    @Override
    public boolean is(LucisType type) {
        return false;
    }
}
