package lucis.compiler.lexer;

import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.io.Reader;

import java.util.function.Supplier;

/**
 * Lexer is used to resolve character stream into lexeme stream.
 *
 * @author Ricardo Evans
 * @version 1.0
 */
@FunctionalInterface
public interface Lexer {
    /**
     * Get the lexeme stream resolved from the given reader
     *
     * @return the lexeme stream
     */
    Supplier<SyntaxTree> resolve(Reader reader);
}
