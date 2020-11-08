package lucis.compiler.parser;

import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.tokenizer.Lexer;
import lucis.compiler.tokenizer.TokenStream;

import java.util.Objects;

@FunctionalInterface
public interface Parser {
    SyntaxTree parse(TokenStream tokenStream);

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
