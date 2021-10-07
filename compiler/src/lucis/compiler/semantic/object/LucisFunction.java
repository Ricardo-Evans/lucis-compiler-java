package lucis.compiler.semantic.object;

import lucis.compiler.semantic.LucisObject;
import lucis.compiler.semantic.LucisType;

public class LucisFunction implements LucisObject {
    public String name;
    public LucisType resultType;
    public String signature;

    public LucisFunction(String name, String signature) {
        this.name = name;
        this.signature = signature;
    }
}
