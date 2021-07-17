package compiler.lexer;

import compiler.entity.Unit;
import compiler.io.Reader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

/**
 * Lexer is used to resolve character stream into lexeme stream.
 *
 * @author Ricardo Evans
 * @version 1.1
 */
@FunctionalInterface
public interface Lexer extends Serializable {
    /**
     * Get the lexeme stream resolved from the given reader, without filter
     *
     * @param reader reader of source
     * @return the lexeme stream
     */
    Stream<Unit> resolve(Reader reader);

    interface Builder {
        Lexer build();

        Builder define(RegularExpression expression, String name, int priority);

        default Builder define(RegularExpression expression, String name) {
            return define(expression, name, 0);
        }

        default Builder define(Class<?> c) {
            for (Field field : c.getFields()) {
                if (!Modifier.isStatic(field.getModifiers()) || !field.canAccess(null)) continue;
                if (field.isAnnotationPresent(LexicalRule.class)) {
                    LexicalRule lexicalRule = field.getAnnotation(LexicalRule.class);
                    defineByAnnotation(field, lexicalRule);
                }
                if (field.isAnnotationPresent(LexicalRules.class)) {
                    LexicalRules lexicalRules = field.getAnnotation(LexicalRules.class);
                    for (LexicalRule lexicalRule : lexicalRules.value())
                        defineByAnnotation(field, lexicalRule);
                }
            }
            return this;
        }

        private void defineByAnnotation(Field field, LexicalRule lexicalRule) {
            try {
                if (RegularExpression.class.isAssignableFrom(field.getType()))
                    define((RegularExpression) field.get(null), lexicalRule.value(), lexicalRule.priority());
                else
                    throw new LexicalException("can only define a lexical rule by regular expression, got " + field.getType() + " instead");
            } catch (IllegalAccessException e) {
                throw new LexicalException(e);
            }
        }
    }
}
