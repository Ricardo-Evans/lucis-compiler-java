package lucis.compiler;

import lucis.compiler.entity.Constants;
import lucis.compiler.entity.Lexeme;
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
            defaultLexer = new DFALexer.Builder()
                    .define(Constants.INTEGER_LITERAL, rule("integer"))
                    .define(Constants.DECIMAL_LITERAL, rule("decimal"))
                    .define(Constants.STRING_LITERAL, rule("string"))
                    .define(Constants.IDENTIFIER, rule("identifier"))

                    .define(Constants.DISCARD, rule("_"))
                    .define(Constants.DOT, rule("."))
                    .define(Constants.COMMA, rule(","))
                    .define(Constants.COLON, rule(":"))
                    .define(Constants.SEMICOLON, rule(";"))
                    .define(Constants.QUESTION, rule("?"))
                    .define(Constants.AT, rule("@"))
                    .define(Constants.ELLIPSIS, rule("..."))
                    .define(Constants.SINGLE_QUOTE, rule("'"))
                    .define(Constants.DOUBLE_QUOTE, rule("\""))
                    .define(Constants.BACK_QUOTE, rule("`"))
                    .define(Constants.ASSIGN, rule("="))
                    .define(Constants.POSITIVE, rule("+"))
                    .define(Constants.NEGATIVE, rule("-"))
                    .define(Constants.MULTIPLY, rule("*"))
                    .define(Constants.DIVISION, rule("/"))

                    .define(Constants.L_ROUND_BRACKET, rule("("))
                    .define(Constants.R_ROUND_BRACKET, rule(")"))
                    .define(Constants.L_SQUARE_BRACKET, rule("["))
                    .define(Constants.R_SQUARE_BRACKET, rule("]"))
                    .define(Constants.L_CURLY_BRACKET, rule("{"))
                    .define(Constants.R_CURLY_BRACKET, rule("}"))
                    .define(Constants.L_ANGLE_BRACKET, rule("<"))
                    .define(Constants.R_ANGLE_BRACKET, rule(">"))

                    .define(Constants.WHITESPACE, rule("whitespace"))
                    .define(Constants.TAB, rule("tab"))
                    .define(Constants.NEWLINE, rule("newline"))
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

    private static LexicalRule rule(String name) {
        return (content, position) -> new Lexeme(name, content, position);
    }
}
