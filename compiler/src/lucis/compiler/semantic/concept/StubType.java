package lucis.compiler.semantic.concept;

import compiler.semantic.SemanticException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface StubType {
    LucisType apply(Map<String, LucisType> types);

    static StubType fromType(LucisType type) {
        return types -> type;
    }

    static StubType fromName(String name) {
        return types -> Optional.ofNullable(types.get(name)).orElseThrow(() -> new SemanticException("type parameter " + name + " is required but not provided"));
    }

    static StubType fromKind(LucisKind kind, StubType... types) {
        return applied -> kind.apply(Arrays.stream(types).map(t -> t.apply(applied)).toArray(LucisType[]::new));
    }
}
