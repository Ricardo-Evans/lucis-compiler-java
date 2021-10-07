package lucis.compiler.semantic.type;

import lucis.compiler.semantic.LucisType;

public class ClassType extends BasicType {
    public ClassType(String module, String name) {
        super(module, name);
    }

    @Override
    public boolean is(LucisType type) {
        return false;
    }
}
