package lucis.compiler.semantic.analyze;

import compiler.entity.BinaryData;
import compiler.semantic.SemanticException;
import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.LucisObject;
import lucis.compiler.semantic.concept.LucisType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Environment {
    private final Map<String, Module> modules = new HashMap<>();
    private final Map<String, Symbol> symbols = new HashMap<>(); // Index table from signature to symbol
    private final Set<String> loadingModules = new HashSet<>();

    public Optional<Module> loadModule(String moduleName) {
        if (loadingModules.contains(moduleName)) throw Utility.recursiveModuleLoading(moduleName).get();
        if (modules.containsKey(moduleName)) return Optional.of(modules.get(moduleName));
        loadingModules.add(moduleName);
        try {
            BinaryData data = BinaryData.wrap(Files.readAllBytes(Path.of(moduleName)));
            Optional<Module> result = Optional.of(ModuleParser.parseModule(this, data));
            result.ifPresent(m -> m.symbols().values().stream().flatMap(Set::stream).forEach(s -> symbols.put(s.signature(), s)));
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            loadingModules.remove(moduleName);
        }
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

    public LucisObject requireObject(String signature) {
        Symbol symbol = symbols.get(signature);
        if (symbol == null) throw Utility.symbolNotFound(signature).get();
        return symbol.value();
    }
}
