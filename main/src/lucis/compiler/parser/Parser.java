package lucis.compiler.parser;

import lucis.compiler.entity.Unit;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
public interface Parser {
    Unit parse(Stream<? extends Unit> lexemes);

    interface Builder {
        Builder define(Grammar grammar);

        default Builder define(String grammar, Function<Unit[], ?> reduction) {
            Objects.requireNonNull(grammar, "the grammar cannot be null");
            Objects.requireNonNull(reduction, "the reduction cannot be null");
            String[] parts = grammar.split(":");
            if (parts.length != 2) throw new IllegalArgumentException("grammar in wrong format: " + grammar);
            return define(new Grammar(parts[0], parts[1].split(" "), reduction));
        }

        Parser build();
    }

}
