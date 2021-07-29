package lucis.compiler.semantic;

import java.util.HashMap;
import java.util.Map;

public record LucisType(String name, Kind kind, LucisModule module, Map<String, Field> fields) {

    public LucisType(String name, Kind kind, LucisModule module) {
        this(name, kind, module, new HashMap<>());
    }

    public record Field() {
    }

    public enum Kind {
        CLASS,
        TRAIT,
    }
}
