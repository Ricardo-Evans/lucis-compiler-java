package lucis.compiler.semantic;

import lucis.compiler.utility.Constants;

public class LucisObject {
    public final String type;

    protected LucisObject(String type) {
        this.type = type;
    }

    public LucisObject() {
        this(Constants.OBJECT_TYPE);
    }
}
