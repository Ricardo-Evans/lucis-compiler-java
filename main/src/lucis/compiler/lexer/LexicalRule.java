package lucis.compiler.lexer;

import lucis.compiler.entity.Lexeme;
import lucis.compiler.entity.Position;
import lucis.compiler.entity.SyntaxTree;

import java.util.function.Function;

public class LexicalRule {
    private Position position = new Position(1, 1);

    public LexicalRule() {
    }

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

    public Function<String, SyntaxTree> normal(String name) {
        return rule(name, (p, s) -> p.move(0, s.length()));
    }

    public Function<String, SyntaxTree> newline(String name) {
        return rule(name, (p, s) -> p.move(1, 1 - p.offset()));
    }
}
