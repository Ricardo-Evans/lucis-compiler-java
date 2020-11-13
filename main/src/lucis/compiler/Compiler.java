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
    private final Lexer lexer;
    private final Parser parser;

    public Compiler() {
        LexicalRule rule = new LexicalRule();
        lexer = new DFALexer.Builder()
                .define(Constants.INTEGER_LITERAL, rule.normal("integer"))
                .define(Constants.DECIMAL_LITERAL, rule.normal("decimal"))
                .define(Constants.IDENTIFIER, rule.normal("identifier"))
                .define(Constants.STRING_LITERAL, rule.normal("string"))
                .define(Constants.NEWLINE, rule.newline("newline"))
                .build();
        parser = new LRParser.Builder("goal").build();
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

}
