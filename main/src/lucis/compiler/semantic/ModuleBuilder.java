package lucis.compiler.semantic;

import lucis.compiler.ir.LucisModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModuleBuilder {
    private String name;
    private final Map<String, TypeBuilder> types = new HashMap<>();

    public void foundType(String name) {
        types.put(name, new TypeBuilder());
    }

    public Optional<TypeBuilder> findType(String name) {
        return Optional.ofNullable(types.get(name));
    }

    public LucisModule build() {
        return null;
    }
}
