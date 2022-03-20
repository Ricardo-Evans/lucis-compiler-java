package lucis.compiler;

import compiler.entity.Unit;
import compiler.lexer.DFALexer;
import compiler.lexer.Lexer;
import compiler.parser.LRParser;
import compiler.parser.Parser;
import compiler.semantic.Analyzer;
import compiler.semantic.BasicAnalyzer;
import lucis.compiler.semantic.analyze.CollectPasses;
import lucis.compiler.semantic.analyze.Environment;
import lucis.compiler.semantic.analyze.InitializePasses;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;
import lucis.compiler.utility.GrammarRules;
import lucis.compiler.utility.LexicalRules;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Compiler {
    private static volatile Lexer defaultLexer = null;
    private static volatile Parser defaultParser = null;
    private static volatile Analyzer<SyntaxTree, Environment> defaultAnalyzer = null;

    private final Lexer lexer;
    private final Parser parser;
    private final Analyzer<SyntaxTree, Environment> analyzer;

    public Compiler() {
        this(defaultLexer(), defaultParser(), defaultAnalyzer());
    }

    public Compiler(Lexer lexer, Parser parser, Analyzer<SyntaxTree, Environment> analyzer) {
        this.lexer = lexer;
        this.parser = parser;
        this.analyzer = analyzer;
    }

    public static Lexer defaultLexer() {
        if (defaultLexer != null) return defaultLexer;
        synchronized (Compiler.class) {
            if (defaultLexer != null) return defaultLexer;
            defaultLexer = new DFALexer.Builder()
                    .define(LexicalRules.class)
                    .build();
        }
        return defaultLexer;
    }

    public static Parser defaultParser() {
        if (defaultParser != null) return defaultParser;
        synchronized (Compiler.class) {
            if (defaultParser != null) return defaultParser;
            defaultParser = new LRParser.Builder("source")
                    .define(GrammarRules.class)
                    .build();
        }
        return defaultParser;
    }

    private static Analyzer<SyntaxTree, Environment> defaultAnalyzer() {
        if (defaultAnalyzer != null) return defaultAnalyzer;
        synchronized (Compiler.class) {
            if (defaultAnalyzer != null) return defaultAnalyzer;
            defaultAnalyzer = new BasicAnalyzer.Builder<SyntaxTree, Environment>()
                    .definePasses(InitializePasses.class)
                    .definePasses(CollectPasses.class)
                    .build();
        }
        return defaultAnalyzer;
    }

    public void compile(File... files) {
        List<SyntaxTree> trees = new LinkedList<>();
        for (File file : files) {
            Stream<Unit> lexemes = lexer.resolve(file.toPath())
                    .filter(unit -> !"line-comment".equals(unit.name()))
                    .filter(unit -> !"block-comment".equals(unit.name()))
                    .filter(unit -> !"blank".equals(unit.name()));
            Source source = parser.parse(lexemes);
            trees.add(source);
        }
        Environment environment = new Environment();
        analyzer.analyze(trees, environment);
        System.out.println("analyze successfully");
        System.out.println("compile successfully");
    }
}
