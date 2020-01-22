package lucis.compiler.parser;

import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.tokenizer.Tokenizer;

import java.util.Objects;

public class DefaultParser implements Parser {
    private Tokenizer tokenizer;

    public DefaultParser(Tokenizer tokenizer) {
        Objects.requireNonNull(tokenizer);
        this.tokenizer = tokenizer;
    }

    @Override
    public SyntaxTree parse() {
        return null;
    }
}
