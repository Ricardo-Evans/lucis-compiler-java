package lucis.compiler.semantic;

import lucis.compiler.semantic.LucisType;

public class LucisVariable {
    public String typename;
    public LucisType type;
    public String name;

    public LucisVariable(String typename, String name) {
        this.typename = typename;
        this.name = name;
    }

    public LucisVariable(LucisType type, String name) {
        this.type = type;
        this.name = name;
    }
}
