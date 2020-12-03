package lucis.compiler.parser;

import lucis.compiler.entity.Handle;
import lucis.compiler.entity.Unit;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
public interface Parser extends Serializable {
    <T> T parse(Stream<? extends Unit> lexemes);

    interface Builder {
        Builder define(Grammar grammar);

        default Builder define(List<Grammar> grammars) {
            grammars.forEach(this::define);
            return this;
        }

        default Builder define(String grammar, Function<Handle, ?> reduction) {
            Objects.requireNonNull(grammar, "the grammar cannot be null");
            Objects.requireNonNull(reduction, "the reduction cannot be null");
            int index = grammar.indexOf(':');
            if (index < 0 || index >= grammar.length())
                throw new IllegalArgumentException("grammar in wrong format: " + grammar);
            String left = grammar.substring(0, index);
            String[] right = grammar.substring(index + 1).split(" ");
            return define(new Grammar(left, right, reduction));
        }

        Parser build();
    }
}
