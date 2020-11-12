package lucis.compiler.parser;

import lucis.compiler.entity.SyntaxTree;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface Parser {
    SyntaxTree parse(Supplier<? extends SyntaxTree> lexemes);

    interface Builder {
        Builder define(Grammar grammar);

        default Builder define(String grammar, Reduction reduction) {
            Objects.requireNonNull(grammar, "the grammar cannot be null");
            Objects.requireNonNull(reduction, "the reduction cannot be null");
            String[] parts = grammar.split(":");
            if (parts.length != 2) throw new IllegalArgumentException("grammar in wrong format: " + grammar);
            return define(new Grammar(parts[0], parts[1].split(" "), reduction));
        }

        Parser build();
    }

    @FunctionalInterface
    interface Reduction {
        SyntaxTree reduce(SyntaxTree... nodes);
    }
}
