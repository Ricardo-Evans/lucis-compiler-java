package compiler.lexer;

import java.util.*;

/**
 * @author owl
 * @author Ricardo Evans
 */
@FunctionalInterface
public interface RegularExpression {
    <T> T visit(Visitor<T> visitor);

    interface Visitor<T> {
        T visitEmpty();

        T visitAny();

        T visitPure(Pure expression);

        T visitRange(Range expression);

        T visitConcatenation(Concatenation expression);

        T visitAlternation(Alternation expression);

        T visitClosure(Closure expression);
    }

    record Empty() implements RegularExpression {
        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitEmpty();
        }
    }

    record Any() implements RegularExpression {
        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitAny();
        }
    }

    record Range(int start, int end) implements RegularExpression {
        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitRange(this);
        }
    }

    record Pure(String content) implements RegularExpression {
        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitPure(this);
        }
    }

    record Concatenation(RegularExpression... expressions) implements RegularExpression {
        public Concatenation {
            if (expressions.length <= 0) throw new IllegalArgumentException();
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitConcatenation(this);
        }
    }

    record Alternation(RegularExpression... expressions) implements RegularExpression {
        public Alternation {
            if (expressions.length <= 0) throw new IllegalArgumentException();
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitAlternation(this);
        }
    }

    record Closure(RegularExpression expression) implements RegularExpression {
        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitClosure(this);
        }
    }

    default RegularExpression concatenate(RegularExpression expression) {
        return RegularExpression.concatenate(this, expression);
    }

    default RegularExpression alternate(RegularExpression expression) {
        return RegularExpression.alternate(this, expression);
    }

    default RegularExpression closure() {
        return RegularExpression.closure(this);
    }

    default RegularExpression multiple() {
        return multiple(this);
    }

    default RegularExpression optional() {
        return optional(this);
    }

    default RegularExpression repeat(int times) {
        return repeat(this, times);
    }

    static RegularExpression empty() {
        return new Empty();
    }

    static RegularExpression any() {
        return new Any();
    }

    static RegularExpression optional(RegularExpression expression) {
        return alternate(empty(), expression);
    }

    static RegularExpression multiple(RegularExpression expression) {
        return concatenate(expression, closure(expression));
    }

    static RegularExpression pure(String content) {
        return new Pure(content);
    }

    static RegularExpression range(int start, int end) {
        return new Range(start, end);
    }

    static RegularExpression negate(int... codepoints) {
        Set<Integer> sorted = new TreeSet<>();
        for (int codepoint : codepoints) sorted.add(codepoint);
        int last = -1;
        List<RegularExpression> expressions = new ArrayList<>(sorted.size() + 1);
        for (int codepoint : sorted) {
            if (codepoint != 0)
                expressions.add(range(last + 1, codepoint));
            last = codepoint;
        }
        expressions.add(range(last + 1, Integer.MAX_VALUE));
        return alternate(expressions.toArray(RegularExpression[]::new));
    }

    static RegularExpression concatenate(RegularExpression... expressions) {
        return new Concatenation(expressions);
    }

    static RegularExpression alternate(RegularExpression... expressions) {
        return new Alternation(expressions);
    }

    static RegularExpression closure(RegularExpression expression) {
        return new Closure(expression);
    }

    static RegularExpression repeat(RegularExpression expression, int times) {
        if (times <= 0)
            throw new IllegalArgumentException("the repeat times must be positive, but get " + times + " instead");
        RegularExpression result = expression;
        for (int i = 1; i < times; ++i) result = result.concatenate(expression);
        return result;
    }
}
