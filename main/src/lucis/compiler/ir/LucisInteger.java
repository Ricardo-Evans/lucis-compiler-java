package lucis.compiler.ir;

import lucis.compiler.utility.Constants;

public class LucisInteger extends LucisObject {
    private byte[] bytes;

    public LucisInteger() {
        super(Constants.INTEGER_TYPE);
    }
}
