package lucis.compiler.ir;

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
