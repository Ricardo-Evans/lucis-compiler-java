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
        SymbolTable table = new SymbolTable();
        while (tokenizer.peek().tag() != Tag.END) {
            tokenizer.skip(Tag.LINE_BREAK);
        }
        return null;
    }

    private DeclarationTree parseDeclaration(SymbolTable table) {
        switch (tokenizer.peek().tag()) {
            case CLASS:
            case TRAIT:
                return parseTypeDeclaration(table);
            case IDENTIFIER:
                if (tokenizer.peek(1).tag() == Tag.IDENTIFIER) {
                    if (tokenizer.peek(2).tag() == Tag.LEFT_PARENTHESIS) {
                        return parseFunctionDeclaration(table);
                    }
                    return parseVariableDeclaration(table);
                }
        }
        return null;
    }

    private StatementTree parseStatement(SymbolTable table) {
        return null;
    }

    private ExpressionTree parseExpression(SymbolTable table) {
        return null;
    }

    private TypeDeclaration parseTypeDeclaration(SymbolTable table) {
        return null;
    }

    private FunctionDeclaration parseFunctionDeclaration(SymbolTable table) {
        return null;
    }

    private VariableDeclaration parseVariableDeclaration(SymbolTable table) {
        return null;
    }
}
