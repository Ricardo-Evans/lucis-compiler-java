package lucis.compiler.ir;

import compiler.entity.Position;

public class LucisVariable {
    public final LucisType type;
    public final String name;
    public final Position position;

    public LucisVariable(LucisType type, String name, Position position) {
        this.type = type;
        this.name = name;
        this.position = position;
    }
}
