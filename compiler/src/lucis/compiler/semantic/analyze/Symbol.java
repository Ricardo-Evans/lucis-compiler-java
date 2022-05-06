package lucis.compiler.semantic.analyze;

import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.LucisObject;
import lucis.compiler.semantic.concept.LucisType;

import java.util.Objects;

public record Symbol(String name, String module, LucisType type, boolean modifiable, LucisObject value) {

    public Symbol(String name, String module, LucisType type) {
        this(name, module, type, true, null);
    }

    public Symbol(String name, String module, LucisType type, boolean modifiable) {
        this(name, module, type, modifiable, null);
    }

    public Symbol(String name, String module, LucisType type, LucisObject value) {
        this(name, module, type, false, value);
    }

    public String signature() {
        return name + Utility.LUCIS_SIGNATURE_DELIMITER + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol symbol)) return false;
        return Objects.equals(name, symbol.name) && Objects.equals(type, symbol.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
