package lucis.compiler;

import compiler.entity.Unit;
import compiler.io.ChannelReader;
import compiler.io.Reader;
import compiler.lexer.DFALexer;
import compiler.lexer.Lexer;
import compiler.parser.LRParser;
import compiler.parser.Parser;
import compiler.semantic.Analyzer;
import compiler.semantic.BasicAnalyzer;
import lucis.compiler.semantic.Environment;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;
import lucis.compiler.utility.AnalyzePasses;
import lucis.compiler.utility.GrammarRules;
import lucis.compiler.utility.LexicalRules;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

public class Compiler {
    private static Lexer defaultLexer = null;
    private static Parser defaultParser = null;
    private static Analyzer<SyntaxTree, Environment> defaultAnalyzer = null;

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
                    .build((object, position) -> {
                        if (object instanceof SyntaxTree) return ((SyntaxTree) object).position(position);
                        else return object;
                    });
        }
        return defaultParser;
    }

    public static Analyzer<SyntaxTree, Environment> defaultAnalyzer() {
        if (defaultAnalyzer != null) return defaultAnalyzer;
        synchronized (Compiler.class) {
            if (defaultAnalyzer != null) return defaultAnalyzer;
            Analyzer.Builder<SyntaxTree, Environment> builder = new BasicAnalyzer.Builder<>();
            builder.definePass(AnalyzePasses.CollectPass);
            defaultAnalyzer = builder.build();
        }
        return defaultAnalyzer;
    }

    public void compile(File file) throws IOException {
        Reader reader = new ChannelReader(FileChannel.open(file.toPath(), StandardOpenOption.READ));
        Stream<Unit> lexemes = lexer.resolve(reader)
                .filter(unit -> !"line-comment".equals(unit.name()))
                .filter(unit -> !"block-comment".equals(unit.name()))
                .filter(unit -> !"blank".equals(unit.name()));
        Source source = parser.parse(lexemes);
        System.out.println("parse successfully");
        Environment environment = new Environment();
        analyzer.analyze(List.of(source), environment);
        System.out.println("analyze successfully");
        System.out.println("compile successfully");
    }
}
