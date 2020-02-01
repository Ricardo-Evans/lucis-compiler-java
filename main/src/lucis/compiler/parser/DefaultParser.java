package lucis.compiler.parser;

import lucis.compiler.entity.Tag;
import lucis.compiler.tokenizer.Tokenizer;
import lucis.compiler.entity.DeclarationTree;
import lucis.compiler.entity.ExpressionTree;
import lucis.compiler.entity.StatementTree;
import lucis.compiler.entity.SyntaxTree;

import java.util.Objects;

public class DefaultParser implements Parser {
    private Tokenizer tokenizer;

    public DefaultParser(Tokenizer tokenizer) {
        Objects.requireNonNull(tokenizer);
        this.tokenizer = tokenizer;
    }

    @Override
    public SyntaxTree parse() {
        while (tokenizer.peek().tag() != Tag.END) {
            tokenizer.skip(Tag.LINE_BREAK);
        }
        return null;
    }

    @Override
    public DeclarationTree parseDeclaration() {
        return null;
    }

    @Override
    public StatementTree parseStatement() {
        return null;
    }

    @Override
    public ExpressionTree parseExpression() {
        return null;
    }
}
