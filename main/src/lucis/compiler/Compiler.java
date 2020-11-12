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
                .define(Constants.STRING_LITERAL, rule.rule("string", (p, s) -> new Position(p.line(), p.offset() + s.length())))
                .define(Constants.NEWLINE, rule.rule("newline", (p, s) -> new Position(p.line() + 1, 1)))
                .build();
    }

    public void compile() {

    }

    private static class LexicalRule {
        private Position position = new Position(1, 1);

        @FunctionalInterface
        public interface Movement {
            Position move(Position position, String content);
        }

        public Function<String, SyntaxTree> rule(String name, Movement movement) {
            return content -> {
                Lexeme lexeme = new Lexeme(name, content, position);
                position = movement.move(position, content);
                return lexeme;
            };
        }
    }
}
