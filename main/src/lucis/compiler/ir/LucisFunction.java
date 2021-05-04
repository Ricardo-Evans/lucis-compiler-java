package lucis.compiler.ir;

import lucis.compiler.utility.Constants;

import java.util.LinkedList;
import java.util.List;

public class LucisFunction extends LucisObject {
    public final String name;
    public final String signature;
    public final List<Instruction> instructions = new LinkedList<>();

    public LucisFunction(String name, String signature) {
        super(Constants.FUNCTION_TYPE);
        this.name = name;
        this.signature = signature;
    }
}
