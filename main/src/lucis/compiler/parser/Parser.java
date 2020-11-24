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
            int index = grammar.indexOf(':');
            if (index < 0 || index >= grammar.length())
                throw new IllegalArgumentException("grammar in wrong format: " + grammar);
            String left = grammar.substring(0, index);
            String right = grammar.substring(index + 1);
            return define(new Grammar(left, right.split(" "), reduction));
        }

        Parser build();
    }

}
