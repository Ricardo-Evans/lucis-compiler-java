package lucis.compiler;

import lucis.compiler.entity.Constants;
import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.io.ChannelReader;
import lucis.compiler.io.Reader;
import lucis.compiler.lexer.DFALexer;
import lucis.compiler.lexer.Lexer;
import lucis.compiler.lexer.LexicalRule;
import lucis.compiler.parser.LRParser;
import lucis.compiler.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;

public class Compiler {
    private static Lexer defaultLexer = null;
    private static Parser defaultParser = null;
    private final Lexer lexer;
    private final Parser parser;

    public Compiler() {
        this(defaultLexer(), defaultParser());
    }

    public Compiler(Lexer lexer, Parser parser) {
        this.lexer = lexer;
        this.parser = parser;
    }

    public void compile(File file) throws IOException {
        Reader reader = new ChannelReader(FileChannel.open(file.toPath(), StandardOpenOption.READ));
        Supplier<SyntaxTree> lexemes = lexer.resolve(reader);
        SyntaxTree lexeme;
        while ((lexeme = lexemes.get()) != null) {
            System.out.println(lexeme);
        }
    }

    public static Lexer defaultLexer() {
        if (defaultLexer != null) return defaultLexer;
        synchronized (Compiler.class) {
            if (defaultLexer != null) return defaultLexer;
            LexicalRule rule = new LexicalRule();
            defaultLexer = new DFALexer.Builder()
                    .define(Constants.INTEGER_LITERAL, rule.normal("integer"))
                    .define(Constants.DECIMAL_LITERAL, rule.normal("decimal"))
                    .define(Constants.STRING_LITERAL, rule.normal("string"))
                    .define(Constants.IDENTIFIER, rule.normal("identifier"))

                    .define(Constants.DISCARD, rule.normal("_"))
                    .define(Constants.DOT, rule.normal("."))
                    .define(Constants.COMMA, rule.normal(","))
                    .define(Constants.COLON, rule.normal(":"))
                    .define(Constants.SEMICOLON, rule.normal(";"))
                    .define(Constants.QUESTION, rule.normal("?"))
                    .define(Constants.AT, rule.normal("@"))
                    .define(Constants.ELLIPSIS, rule.normal("..."))
                    .define(Constants.SINGLE_QUOTE, rule.normal("'"))
                    .define(Constants.DOUBLE_QUOTE, rule.normal("\""))
                    .define(Constants.BACK_QUOTE, rule.normal("`"))
                    .define(Constants.ASSIGN, rule.normal("="))
                    .define(Constants.POSITIVE, rule.normal("+"))
                    .define(Constants.NEGATIVE, rule.normal("-"))
                    .define(Constants.MULTIPLY, rule.normal("*"))
                    .define(Constants.DIVISION, rule.normal("/"))

                    .define(Constants.L_ROUND_BRACKET, rule.normal("("))
                    .define(Constants.R_ROUND_BRACKET, rule.normal(")"))
                    .define(Constants.L_SQUARE_BRACKET, rule.normal("["))
                    .define(Constants.R_SQUARE_BRACKET, rule.normal("]"))
                    .define(Constants.L_CURLY_BRACKET, rule.normal("{"))
                    .define(Constants.R_CURLY_BRACKET, rule.normal("}"))
                    .define(Constants.L_ANGLE_BRACKET, rule.normal("<"))
                    .define(Constants.R_ANGLE_BRACKET, rule.normal(">"))

                    .define(Constants.WHITESPACE, rule.normal("whitespace"))
                    .define(Constants.TAB, rule.normal("tab"))
                    .define(Constants.NEWLINE, rule.newline("newline"))
                    .build();
        }
        return defaultLexer;
    }

    public static Parser defaultParser() {
        if (defaultParser != null) return defaultParser;
        synchronized (Compiler.class) {
            if (defaultParser != null) return defaultParser;
            defaultParser = new LRParser.Builder("goal").build();
        }
        return defaultParser;
    }
}
