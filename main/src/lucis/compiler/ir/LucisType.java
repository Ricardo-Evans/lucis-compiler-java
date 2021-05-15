package lucis.compiler.ir;

import java.util.HashMap;
import java.util.Map;

public class LucisType extends LucisObject {
    public String module;
    public String name;
    public Map<String, LucisVariable> fieldsMap = new HashMap<>();

    public LucisType(String name, String module) {
        this.name = name;
        this.module = module;
    }
}
