package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.concept.Element;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public record Symbol(String name, Set<Element> candidates) {
    public Symbol(String name) {
        this(name, new HashSet<>());
    }

    public Element unique() {
        if (candidates.size() != 1) throw new AssertionError("candidates not unique");
        return candidates.iterator().next();
    }

    public Stream<Element> findElement(Function<Stream<Element>, Stream<Element>> filter) {
        return filter.apply(candidates.stream());
    }

    public void foundElement(Iterable<Element> elements) {
        elements.forEach(this::foundElement);
    }

    public void foundElement(Element element) {
        if (!candidates.add(element)) throw new SemanticException("element: " + element + " already defined");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol symbol)) return false;
        return Objects.equals(name, symbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
