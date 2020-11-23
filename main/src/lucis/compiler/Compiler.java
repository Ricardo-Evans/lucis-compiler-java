package lucis.compiler;

import lucis.compiler.entity.*;
import lucis.compiler.io.ChannelReader;
import lucis.compiler.io.Reader;
import lucis.compiler.lexer.DFALexer;
import lucis.compiler.lexer.Lexer;
import lucis.compiler.parser.LRParser;
import lucis.compiler.parser.Parser;
import lucis.compiler.utility.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Compiler {
    private static Lexer defaultLexer = null;
    private static Parser defaultParser = null;
    private final Lexer lexer;
    private final Parser parser;

    public Compiler() {
        this(defaultLexer(), null);
    }

    public Compiler(Lexer lexer, Parser parser) {
        this.lexer = lexer;
        this.parser = parser;
    }

    public void compile(File file) throws IOException {
        Reader reader = new ChannelReader(FileChannel.open(file.toPath(), StandardOpenOption.READ));
        Stream<Unit> lexemes = lexer.resolve(reader);
        lexemes.forEach(System.out::println);
        // parser.parse(lexemes);
    }

    public static Lexer defaultLexer() {
        if (defaultLexer != null) return defaultLexer;
        synchronized (Compiler.class) {
            if (defaultLexer != null) return defaultLexer;
            defaultLexer = new DFALexer.Builder()
                    .define(Constants.INTEGER_LITERAL, "integer")
                    .define(Constants.DECIMAL_LITERAL, "decimal")
                    .define(Constants.NORMAL_STRING_LITERAL, "normal-string")
                    .define(Constants.RAW_STRING_LITERAL, "raw-string")
                    .define(Constants.IDENTIFIER, "identifier", -1)

                    .define(Constants.DISCARD, "_")
                    .define(Constants.DOT, ".")
                    .define(Constants.COMMA, ",")
                    .define(Constants.COLON, ":")
                    .define(Constants.SEMICOLON, ";")
                    .define(Constants.QUESTION, "?")
                    .define(Constants.AT, "@")
                    .define(Constants.ELLIPSIS, "...")
                    .define(Constants.SINGLE_QUOTE, "'")
                    .define(Constants.DOUBLE_QUOTE, "\"")
                    .define(Constants.BACK_QUOTE, "`")
                    .define(Constants.ASSIGN, "=")
                    .define(Constants.POSITIVE, "+")
                    .define(Constants.NEGATIVE, "-")
                    .define(Constants.MULTIPLY, "*")
                    .define(Constants.DIVISION, "/")
                    .define(Constants.REMAINDER, "%")
                    .define(Constants.AND, "&")
                    .define(Constants.OR, "|")
                    .define(Constants.NOT, "!")

                    .define(Constants.L_ROUND_BRACKET, "(")
                    .define(Constants.R_ROUND_BRACKET, ")")
                    .define(Constants.L_SQUARE_BRACKET, "[")
                    .define(Constants.R_SQUARE_BRACKET, "]")
                    .define(Constants.L_CURLY_BRACKET, "{")
                    .define(Constants.R_CURLY_BRACKET, "}")
                    .define(Constants.L_ANGLE_BRACKET, "<")
                    .define(Constants.R_ANGLE_BRACKET, ">")

                    .define(Constants.IN, "in")
                    .define(Constants.IS, "is")
                    .define(Constants.AS, "as")
                    .define(Constants.IF, "if")
                    .define(Constants.ELSE, "else")
                    .define(Constants.WHEN, "when")
                    .define(Constants.WHILE, "while")
                    .define(Constants.BREAK, "break")
                    .define(Constants.CLASS, "class")
                    .define(Constants.TRAIT, "trait")
                    .define(Constants.IMPORT, "import")
                    .define(Constants.EXPORT, "export")
                    .define(Constants.LAMBDA, "lambda")
                    .define(Constants.ASSERT, "assert")
                    .define(Constants.NATIVE, "native")
                    .define(Constants.RETURN, "return")

                    .define(Constants.LINE_COMMENT, "line-comment", -1)
                    .define(Constants.BLOCK_COMMENT, "block-comment")
                    .define(Constants.BLANK, "blank")
                    .build();
        }
        return defaultLexer;
    }

    public static Parser defaultParser() {
        if (defaultParser != null) return defaultParser;
        synchronized (Compiler.class) {
            if (defaultParser != null) return defaultParser;
            defaultParser = new LRParser.Builder("source")
                    .build();
        }
        return defaultParser;
    }

    private static class Reductions {
        private static Source source(Source source, Statement statement) {
            source.statements().add(statement);
            return source;
        }

        private static Source source() {
            return new Source(new LinkedList<>());
        }

        private static Statement statement(FunctionStatement function) {
            return new Statement(function);
        }

        private static Expression expression(Lexeme lexeme) {
            return null;
        }
    }
}
