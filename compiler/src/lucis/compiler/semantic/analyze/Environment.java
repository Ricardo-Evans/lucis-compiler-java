package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Environment {
    private final Map<String, Module> modules = new HashMap<>();

    public Optional<Module> loadModule(String name) {
        return Optional.empty();
    }

    public Optional<Module> findModule(String name) {
        Objects.requireNonNull(name);
        return Optional.ofNullable(modules.get(name)).or(() -> loadModule(name));
    }

    public Module foundModule(String name) {
        Objects.requireNonNull(name);
        modules.putIfAbsent(name, new Module(name));
        return modules.get(name);
    }

    public Module requireModule(String name) {
        return findModule(name).orElseThrow(Utility.moduleNotFound(name));
    }

    public Optional<Module> findCoreModule() {
        return findModule(Utility.LUCIS_CORE);
    }

    public Module requireCoreModule() {
        return findCoreModule().orElseThrow(Utility.moduleNotFound(Utility.LUCIS_CORE));
    }
}