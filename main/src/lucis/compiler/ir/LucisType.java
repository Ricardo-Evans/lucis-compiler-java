package lucis.compiler.ir;

import lucis.compiler.utility.Constants;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LucisType extends LucisObject {
    public final String name;
    public final List<String> parents;
    public final Map<String, LucisVariable> fieldsMap = new HashMap<>();

    public LucisType(String name) {
        this(name, new LinkedList<>(List.of(Constants.OBJECT_TYPE)));
    }

    public LucisType(String name, List<String> parents) {
        super(Constants.TYPE_TYPE);
        this.name = name;
        this.parents = parents;
    }
}
