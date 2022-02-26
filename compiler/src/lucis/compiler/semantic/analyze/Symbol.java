package lucis.compiler.semantic.analyze;

import lucis.compiler.semantic.concept.LucisObject;
import lucis.compiler.semantic.concept.LucisType;

import java.util.Objects;

public record Symbol(String name, LucisType type, boolean modifiable, LucisObject value) {

    public Symbol(String name, LucisType type) {
        this(name, type, true, null);
    }

    public Symbol(String name, LucisType type, boolean modifiable) {
        this(name, type, modifiable, null);
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
