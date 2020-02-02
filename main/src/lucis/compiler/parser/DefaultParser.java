package lucis.compiler.parser;

import lucis.compiler.entity.*;
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
        while (tokenizer.peek().tag() != Tag.END) {
            tokenizer.skip(Tag.LINE_BREAK);
        }
        return null;
    }

    @Override
    public DeclarationTree parseDeclaration() {
        switch (tokenizer.peek().tag()) {
            case CLASS:
            case TRAIT:
                return parseTypeDeclaration();
            case IDENTIFIER:
                if (tokenizer.peek(1).tag() == Tag.IDENTIFIER) {
                    if (tokenizer.peek(2).tag() == Tag.LEFT_PARENTHESIS) {
                        return parseFunctionDeclaration();
                    }
                    return parseVariableDeclaration();
                }
        }
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

    private TypeDeclaration parseTypeDeclaration() {
        return null;
    }

    private FunctionDeclaration parseFunctionDeclaration() {
        return null;
    }

    private VariableDeclaration parseVariableDeclaration() {
        return null;
    }
}
