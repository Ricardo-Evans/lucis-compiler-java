package lucis.compiler.semantic;

import compiler.semantic.SemanticException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record LucisSymbol(String name, String module, Set<LucisElement> candidates) {
    public LucisSymbol(String name) {
        this(name, null);
    }

    public LucisSymbol(String name, String module) {
        this(name, module, new HashSet<>());
    }

    public String signature() {
        if (module == null) return name;
        return module + ":" + name;
    }

    public Stream<LucisElement> findElement(Function<Stream<LucisElement>, Stream<LucisElement>> filter) {
        return filter.apply(candidates.stream());
    }

    public void foundElement(LucisElement element) {
        if (!candidates.add(element)) throw new SemanticException("element: " + element + " already defined");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LucisSymbol symbol)) return false;
        return Objects.equals(name, symbol.name) && Objects.equals(module, symbol.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, module);
    }
}
