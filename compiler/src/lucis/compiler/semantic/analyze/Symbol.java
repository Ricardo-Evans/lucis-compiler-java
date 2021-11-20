package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.concept.Element;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public record Symbol(String name, String module, Set<Element> candidates) {
    public Symbol(String name) {
        this(name, null);
    }

    public Symbol(String name, String module) {
        this(name, module, new HashSet<>());
    }

    public String signature() {
        if (module == null) return name;
        return module + ":" + name;
    }

    public Stream<Element> findElement(Function<Stream<Element>, Stream<Element>> filter) {
        return filter.apply(candidates.stream());
    }

    public void foundElement(Element element) {
        if (!candidates.add(element)) throw new SemanticException("element: " + element + " already defined");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol symbol)) return false;
        return Objects.equals(name, symbol.name) && Objects.equals(module, symbol.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, module);
    }
}
