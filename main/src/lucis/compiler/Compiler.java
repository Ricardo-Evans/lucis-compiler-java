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
                    .define("source:statement-list", handle -> new Source(handle.at(0)))

                    .define(list("statement"))
                    .define(list("parameter", ",", true))
                    .define(list("expression", ",", true))
                    .define(list("identifier", ",", false))

                    .define("statement:assign-statement", handle -> handle.at(0))
                    .define("statement:branch-statement", handle -> handle.at(0))
                    .define("statement:define-statement", handle -> handle.at(0))
                    .define("statement:export-statement", handle -> handle.at(0))
                    .define("statement:import-statement", handle -> handle.at(0))
                    .define("statement:return-statement", handle -> handle.at(0))
                    .define("statement:discard-statement", handle -> handle.at(0))
                    .define("statement:function-statement", handle -> handle.at(0))
                    .define("statement:expression-statement", handle -> handle.at(0))
                    .define("assign-statement:identifier = expression", handle -> new AssignStatement(handle.at(0), handle.at(2)))
                    .define("branch-statement:if expression : statement", handle -> new BranchStatement(handle.at(1), handle.at(3), null))
                    .define("branch-statement:if expression : statement else statement", handle -> new BranchStatement(handle.at(1), handle.at(3), handle.at(5)))
                    .define("define-statement:identifier-expression identifier = expression", handle -> new DefineStatement(handle.at(0), handle.at(1), handle.at(3)))
                    .define("define-statement:identifier : = expression", handle -> new DefineStatement(null, handle.at(0), handle.at(3)))
                    .define("return-statement:return expression", handle -> new ReturnStatement(handle.at(1)))
                    .define("discard-statement:_ = expression", handle -> new DiscardStatement(handle.at(2)))
                    .define("function-statement:identifier-expression identifier ( parameter-list ) : expression", handle -> new FunctionStatement(handle.at(0), handle.at(1), handle.at(3), handle.at(6)))
                    .define("parameter:identifier-expression identifier", handle -> new FunctionStatement.Parameter(handle.at(0), handle.at(1)))
                    .define("expression-statement:expression", handle -> new ExpressionStatement(handle.at(0)))

                    .define(prior("expression", priorities))

                    .define("block-expression:{ statement-list }", handle -> new BlockExpression(handle.at(1)))
                    .define("index-expression:expression-0 [ expression ]", handle -> new IndexExpression(handle.at(0), handle.at(2)))
                    .define("bracket-expression:( expression )", handle -> handle.at(1))
                    .define("literal-expression:integer", handle -> new LiteralExpression(LiteralExpression.Type.INTEGER, handle.at(0)))
                    .define("literal-expression:decimal", handle -> new LiteralExpression(LiteralExpression.Type.DECIMAL, handle.at(0)))
                    .define("literal-expression:normal-string", handle -> new LiteralExpression(LiteralExpression.Type.STRING, handle.at(0)))
                    .define("literal-expression:raw-string", handle -> new LiteralExpression(LiteralExpression.Type.STRING, handle.at(0)))
                    .define("function-expression:expression-0 ( expression-list )", handle -> new FunctionExpression(handle.at(0), handle.at(2)))
                    .define("identifier-expression:identifier", handle -> new IdentifierExpression(handle.at(0)))
                    .define("identifier-expression:expression-0 . identifier", handle -> new IdentifierExpression(handle.at(0), handle.at(2)))

                    .define("negation-expression:! expression-1", handle -> new SingleOperatorExpression(OperatorExpression.Operator.NOT, handle.at(1)))
                    .define("negation-expression:not expression-1", handle -> new SingleOperatorExpression(OperatorExpression.Operator.NOT, handle.at(1)))
                    .define("negative-expression:- expression-1", handle -> new SingleOperatorExpression(OperatorExpression.Operator.NEGATIVE, handle.at(1)))
                    .define("positive-expression:+ expression-1", handle -> new SingleOperatorExpression(OperatorExpression.Operator.POSITIVE, handle.at(1)))

                    .define("division-expression:expression-2 / expression-1", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.DIVISION, handle.at(0), handle.at(2)))
                    .define("multiply-expression:expression-2 * expression-1", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.MULTIPLY, handle.at(0), handle.at(2)))
                    .define("remainder-expression:expression-2 % expression-1", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.REMAINDER, handle.at(0), handle.at(2)))

                    .define("plus-expression:expression-3 + expression-2", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.PLUS, handle.at(0), handle.at(2)))
                    .define("minus-expression:expression-3 - expression-2", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.MINUS, handle.at(0), handle.at(2)))

                    .define("compare-expression:expression-4 < expression-3", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.LESS, handle.at(0), handle.at(2)))
                    .define("compare-expression:expression-4 > expression-3", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.GREATER, handle.at(0), handle.at(2)))
                    .define("compare-expression:expression-4 < = expression-3", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.LESS_EQUAL, handle.at(0), handle.at(3)))
                    .define("compare-expression:expression-4 > = expression-3", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.GREATER_EQUAL, handle.at(0), handle.at(3)))

                    .define("equality-expression:expression-5 = = expression-4", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.EQUAL, handle.at(0), handle.at(3)))
                    .define("equality-expression:expression-5 ! = expression-4", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.NOT_EQUAL, handle.at(0), handle.at(3)))

                    .define("and-expression:expression-6 & expression-5", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.AND, handle.at(0), handle.at(2)))
                    .define("and-expression:expression-6 and expression-5", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.AND, handle.at(0), handle.at(2)))

                    .define("or-expression:expression-7 | expression-6", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.OR, handle.at(0), handle.at(2)))
                    .define("or-expression:expression-7 or expression-6", handle -> new DoubleOperatorExpression(OperatorExpression.Operator.OR, handle.at(0), handle.at(2)))
                    .build();
        }
        return defaultParser;
    }

    private static List<Grammar> list(String name) {
        String listName = name + "-list";
        Grammar grammar1 = new Grammar(listName, new String[]{listName, name}, handle -> {
            List<Object> list = handle.at(0);
            list.add(handle.at(1));
            return list;
        });
        Grammar grammar2 = new Grammar(listName, new String[]{}, handle -> new LinkedList<>());
        return List.of(grammar1, grammar2);
    }

    private static List<Grammar> list(String name, String split, boolean empty) {
        String listName = name + "-list";
        Grammar grammar1 = new Grammar(listName, new String[]{listName, split, name}, handle -> {
            List<Object> list = handle.at(0);
            list.add(handle.at(2));
            return list;
        });
        Grammar grammar2 = new Grammar(listName, new String[]{name}, handle -> new LinkedList<>(List.of((Object) handle.at(0))));
        if (!empty) return List.of(grammar1, grammar2);
        Grammar grammar3 = new Grammar(listName, new String[]{}, handle -> new LinkedList<>());
        return List.of(grammar1, grammar2, grammar3);
    }

    private static List<Grammar> prior(String name, Map<String, Integer> priorities) {
        List<Grammar> grammars = new LinkedList<>();
        priorities.forEach((s, i) -> grammars.add(new Grammar(name + "-" + i, new String[]{s}, handle -> handle.at(0))));
        Iterator<Integer> iterator = new HashSet<>(priorities.values()).stream().sorted(Comparator.reverseOrder()).iterator();
        String s0 = name;
        while (iterator.hasNext()) {
            String s = name + "-" + iterator.next();
            grammars.add(new Grammar(s0, new String[]{s}, handle -> handle.at(0)));
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
        Source source = parser.parse(lexemes);
        System.out.println("parse successfully");
    }
}
