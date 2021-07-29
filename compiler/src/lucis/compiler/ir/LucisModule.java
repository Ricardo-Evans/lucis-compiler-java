package lucis.compiler.ir;

import lucis.compiler.utility.Constants;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LucisModule extends LucisObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 8968825529218018034L;
    public final String name;
    private final Map<LucisObject, Integer> constants = new HashMap<>();
    private final Map<String, Integer> variables = new HashMap<>();
    private final List<Instruction> instructions = new LinkedList<>();

    public LucisModule(String name) {
        super(Constants.MODULE_TYPE);
        this.name = name;
    }
}
