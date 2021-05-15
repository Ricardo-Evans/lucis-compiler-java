package lucis.compiler.ir;

public class LucisFunction extends LucisObject {
    public String name;
    public String signature;

    public LucisFunction(String name, String signature) {
        this.name = name;
        this.signature = signature;
    }
}
