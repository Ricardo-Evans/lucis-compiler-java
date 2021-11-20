package lucis.compiler.semantic.concept;

public class LucisFunction implements LucisObject {
    public String name;
    private LucisType parameterType;
    public LucisType resultType;
    private int stackSize;
}
