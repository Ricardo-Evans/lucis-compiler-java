package lucis.compiler;

import lucis.compiler.entity.*;
import lucis.compiler.io.ChannelReader;
import lucis.compiler.io.Reader;
import lucis.compiler.lexer.DFALexer;
import lucis.compiler.lexer.Lexer;
import lucis.compiler.parser.Grammar;
import lucis.compiler.parser.LRParser;
import lucis.compiler.parser.Parser;
import lucis.compiler.syntax.*;
import lucis.compiler.utility.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class Compiler {
    private static final Map<String, Integer> priorities = new HashMap<>();
    private static Lexer defaultLexer = null;
    private static Parser defaultParser = null;

    static {
        priorities.put("block-expression", 0);
        priorities.put("index-expression", 0);
        priorities.put("bracket-expression", 0);
        priorities.put("literal-expression", 0);
        priorities.put("function-expression", 0);
        priorities.put("identifier-expression", 0);
        priorities.put("negation-expression", 1);
        priorities.put("negative-expression", 1);
        priorities.put("positive-expression", 1);
        priorities.put("division-expression", 2);
        priorities.put("multiply-expression", 2);
        priorities.put("remainder-expression", 2);
        priorities.put("plus-expression", 3);
        priorities.put("minus-expression", 3);
        priorities.put("compare-expression", 4);
        priorities.put("equality-expression", 5);
        priorities.put("and-expression", 6);
        priorities.put("or-expression", 7);
    }

    private final Lexer lexer;
    private final Parser parser;

    public Compiler() {
        this(defaultLexer(), defaultParser());
    }

    public Compiler(Lexer lexer, Parser parser) {
        this.lexer = lexer;
        this.parser = parser;
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

                    .define(Constants.AS, "as")
                    .define(Constants.IF, "if")
                    .define(Constants.IN, "in")
                    .define(Constants.IS, "is")
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
                    .define("source:statement-list", units -> {
                        List<Statement> statements = units[0].value();
                        return new Source(statements);
                    })
                    .define(list("statement"))
                    .define(list("parameter", ",", true))
                    .define(list("expression", ",", true))
                    .define(list("identifier", ",", false))

                    .define("statement:assign-statement", units -> units[0].value())
                    .define("statement:branch-statement", units -> units[0].value())
                    .define("statement:define-statement", units -> units[0].value())
                    .define("statement:export-statement", units -> units[0].value())
                    .define("statement:import-statement", units -> units[0].value())
                    .define("statement:return-statement", units -> units[0].value())
                    .define("statement:discard-statement", units -> units[0].value())
                    .define("statement:function-statement", units -> units[0].value())
                    .define("statement:expression-statement", units -> units[0].value())
                    .define("assign-statement:identifier = expression", units -> new AssignStatement(units[0].value(), units[2].value()))
                    .define("branch-statement:if expression : statement", units -> new BranchStatement(units[1].value(), units[3].value(), null))
                    .define("branch-statement:if expression : statement else statement", units -> new BranchStatement(units[1].value(), units[3].value(), units[5].value()))
                    .define("define-statement:identifier-expression identifier = expression", units -> new DefineStatement(units[0].value(), units[1].value(), units[3].value()))
                    .define("define-statement:identifier : = expression", units -> new DefineStatement(null, units[0].value(), units[3].value()))
                    .define("return-statement:return expression", units -> new ReturnStatement(units[1].value()))
                    .define("discard-statement:_ = expression", units -> new DiscardStatement(units[2].value()))
                    .define("function-statement:identifier-expression identifier ( parameter-list ) : expression", units -> new FunctionStatement(units[0].value(), units[1].value(), units[3].value(), units[6].value()))
                    .define("parameter:identifier-expression identifier", units -> new FunctionStatement.Parameter(units[0].value(), units[1].value()))
                    .define("expression-statement:expression", units -> new ExpressionStatement(units[0].value()))

                    .define(prior("expression", priorities))

                    .define("block-expression:{ statement-list }", units -> new BlockExpression(units[1].value()))
                    .define("index-expression:expression-0 [ expression ]", units -> new IndexExpression(units[0].value(), units[2].value()))
                    .define("bracket-expression:( expression )", units -> units[1].value())
                    .define("literal-expression:integer", units -> new LiteralExpression(LiteralExpression.Type.INTEGER, units[0].value()))
                    .define("literal-expression:decimal", units -> new LiteralExpression(LiteralExpression.Type.DECIMAL, units[0].value()))
                    .define("literal-expression:normal-string", units -> new LiteralExpression(LiteralExpression.Type.STRING, units[0].value()))
                    .define("literal-expression:raw-string", units -> new LiteralExpression(LiteralExpression.Type.STRING, units[0].value()))
                    .define("function-expression:expression-0 ( expression-list )", units -> new FunctionExpression(units[0].value(), units[2].value()))
                    .define("identifier-expression:identifier", units -> new IdentifierExpression(units[0].value()))
                    .define("identifier-expression:expression-0 . identifier", units -> new IdentifierExpression(units[0].value(), units[2].value()))

                    .define("negation-expression:! expression-1", units -> new SingleOperatorExpression(OperatorExpression.Operator.NOT, units[1].value()))
                    .define("negation-expression:not expression-1", units -> new SingleOperatorExpression(OperatorExpression.Operator.NOT, units[1].value()))
                    .define("negative-expression:- expression-1", units -> new SingleOperatorExpression(OperatorExpression.Operator.NEGATIVE, units[1].value()))
                    .define("positive-expression:+ expression-1", units -> new SingleOperatorExpression(OperatorExpression.Operator.POSITIVE, units[1].value()))

                    .define("division-expression:expression-2 / expression-1", units -> new DoubleOperatorExpression(OperatorExpression.Operator.DIVISION, units[0].value(), units[2].value()))
                    .define("multiply-expression:expression-2 * expression-1", units -> new DoubleOperatorExpression(OperatorExpression.Operator.MULTIPLY, units[0].value(), units[2].value()))
                    .define("remainder-expression:expression-2 % expression-1", units -> new DoubleOperatorExpression(OperatorExpression.Operator.REMAINDER, units[0].value(), units[2].value()))

                    .define("plus-expression:expression-3 + expression-2", units -> new DoubleOperatorExpression(OperatorExpression.Operator.PLUS, units[0].value(), units[2].value()))
                    .define("minus-expression:expression-3 - expression-2", units -> new DoubleOperatorExpression(OperatorExpression.Operator.MINUS, units[0].value(), units[2].value()))

                    .define("compare-expression:expression-4 < expression-3", units -> new DoubleOperatorExpression(OperatorExpression.Operator.LESS, units[0].value(), units[2].value()))
                    .define("compare-expression:expression-4 > expression-3", units -> new DoubleOperatorExpression(OperatorExpression.Operator.GREATER, units[0].value(), units[2].value()))
                    .define("compare-expression:expression-4 < = expression-3", units -> new DoubleOperatorExpression(OperatorExpression.Operator.LESS_EQUAL, units[0].value(), units[3].value()))
                    .define("compare-expression:expression-4 > = expression-3", units -> new DoubleOperatorExpression(OperatorExpression.Operator.GREATER_EQUAL, units[0].value(), units[3].value()))

                    .define("equality-expression:expression-5 = = expression-4", units -> new DoubleOperatorExpression(OperatorExpression.Operator.EQUAL, units[0].value(), units[3].value()))
                    .define("equality-expression:expression-5 ! = expression-4", units -> new DoubleOperatorExpression(OperatorExpression.Operator.NOT_EQUAL, units[0].value(), units[3].value()))

                    .define("and-expression:expression-6 & expression-5", units -> new DoubleOperatorExpression(OperatorExpression.Operator.AND, units[0].value(), units[2].value()))
                    .define("and-expression:expression-6 and expression-5", units -> new DoubleOperatorExpression(OperatorExpression.Operator.AND, units[0].value(), units[2].value()))

                    .define("or-expression:expression-7 | expression-6", units -> new DoubleOperatorExpression(OperatorExpression.Operator.OR, units[0].value(), units[2].value()))
                    .define("or-expression:expression-7 or expression-6", units -> new DoubleOperatorExpression(OperatorExpression.Operator.OR, units[0].value(), units[2].value()))
                    .build();
        }
        return defaultParser;
    }

    private static List<Grammar> list(String name) {
        String listName = name + "-list";
        Grammar grammar1 = new Grammar(listName, new String[]{listName, name}, units -> {
            List<Object> list = units[0].value();
            list.add(units[1].value());
            return list;
        });
        Grammar grammar2 = new Grammar(listName, new String[]{}, units -> new LinkedList<>());
        return List.of(grammar1, grammar2);
    }

    private static List<Grammar> list(String name, String split, boolean empty) {
        String listName = name + "-list";
        Grammar grammar1 = new Grammar(listName, new String[]{listName, split, name}, units -> {
            List<Object> list = units[0].value();
            list.add(units[2].value());
            return list;
        });
        Grammar grammar2 = new Grammar(listName, new String[]{name}, units -> new LinkedList<>(List.of((Object) units[0].value())));
        if (!empty) return List.of(grammar1, grammar2);
        Grammar grammar3 = new Grammar(listName, new String[]{}, units -> new LinkedList<>());
        return List.of(grammar1, grammar2, grammar3);
    }

    private static List<Grammar> prior(String name, Map<String, Integer> priorities) {
        List<Grammar> grammars = new LinkedList<>();
        priorities.forEach((s, i) -> grammars.add(new Grammar(name + "-" + i, new String[]{s}, units -> units[0].value())));
        Iterator<Integer> iterator = new HashSet<>(priorities.values()).stream().sorted(Comparator.reverseOrder()).iterator();
        String s0 = name;
        while (iterator.hasNext()) {
            String s = name + "-" + iterator.next();
            grammars.add(new Grammar(s0, new String[]{s}, units -> units[0].value()));
            s0 = s;
        }
        return grammars;
    }

    public void compile(File file) throws IOException {
        Reader reader = new ChannelReader(FileChannel.open(file.toPath(), StandardOpenOption.READ));
        Stream<Unit> lexemes = lexer.resolve(reader)
                .filter(unit -> !"line-comment".equals(unit.name()))
                .filter(unit -> !"block-comment".equals(unit.name()))
                .filter(unit -> !"blank".equals(unit.name()));
        Unit unit = parser.parse(lexemes);
        System.out.println("parse successfully");
    }
}
