package lucis.compiler.lexer;

import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.entity.Unit;
import lucis.compiler.io.Reader;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

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
    Supplier<Unit> resolve(Reader reader);

    /**
     * Get the filtered lexeme stream resolved from the given reader
     *
     * @param reader  reader of source
     * @param ignores set of ignores
     * @return the lexeme stream
     */
    default Supplier<Unit> resolve(Reader reader, Set<String> ignores) {
        Objects.requireNonNull(ignores);
        Supplier<Unit> lexemes = resolve(reader);
        return () -> {
            Unit unit;
            do {
                unit = lexemes.get();
                if (unit == null) return null;
            } while (ignores.contains(unit.name()));
            return unit;
        };
    }

    interface Builder {
        Lexer build();

        Builder define(RegularExpression expression, String name);
    }
}
