package lucis.compiler.lexer;

import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.entity.Unit;
import lucis.compiler.io.Reader;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Lexer is used to resolve character stream into lexeme stream.
 *
 * @author Ricardo Evans
 * @version 1.1
 */
@FunctionalInterface
public interface Lexer {
    /**
     * Get the lexeme stream resolved from the given reader, without filter
     *
     * @param reader reader of source
     * @return the lexeme stream
     */
    Stream<Unit> resolve(Reader reader);

    interface Builder {
        Lexer build();

        Builder define(RegularExpression expression, String name);
    }
}
