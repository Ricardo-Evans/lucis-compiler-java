package lucis.compiler;

import lucis.compiler.entity.Constants;
import lucis.compiler.entity.Lexeme;
import lucis.compiler.entity.Position;
import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.lexer.DFALexer;
import lucis.compiler.lexer.Lexer;

import java.util.function.Function;

public class Compiler {
    public Compiler() {
        LexicalRule rule = new LexicalRule();
        Lexer lexer = new DFALexer.Builder()
                .define(Constants.STRING_LITERAL, rule.common("string"))
                .define(Constants.NEWLINE, rule.newline("newline"))
                .build();
    }

    public void compile() {

    }

    private static class LexicalRule {
        private int line = 1;
        private int offset = 1;

        public Function<String, SyntaxTree> common(String name) {
            return content -> {
                Lexeme lexeme = new Lexeme(name, content, new Position(line, offset));
                offset += content.length();
                return lexeme;
            };
        }

        public Function<String, SyntaxTree> newline(String name) {
            return content -> {
                Lexeme lexeme = new Lexeme(name, content, new Position(line, offset));
                ++line;
                offset = 1;
                return lexeme;
            };
        }

        public Function<String, SyntaxTree> reset(String name) {
            return content -> {
                Lexeme lexeme = new Lexeme(name, content, new Position(line, offset));
                line = 1;
                offset = 1;
                return lexeme;
            };
        }
    }
}
