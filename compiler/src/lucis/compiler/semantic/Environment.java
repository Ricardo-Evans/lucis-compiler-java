package lucis.compiler.semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Environment {
    private final Map<String, LucisModule> modules = new HashMap<>();

    public void load(String name) {

    }

    public Optional<LucisModule> findModule(String name) {
        Objects.requireNonNull(name);
        return Optional.ofNullable(modules.get(name));
    }

    public LucisModule foundModule(String name) {
        Objects.requireNonNull(name);
        modules.putIfAbsent(name, new LucisModule(name));
        return modules.get(name);
    }
}
