package lucis.compiler.syntax;

public abstract class OperatorExpression implements Expression {
    public enum Operator {
        AND,
        DIVISION,
        EQUAL,
        GREATER,
        GREATER_EQUAL,
        LESS,
        LESS_EQUAL,
        MINUS,
        MULTIPLY,
        NEGATIVE,
        NOT,
        NOT_EQUAL,
        OR,
        PLUS,
        POSITIVE,
        REMAINDER,
    }

    public final Operator operator;

    public OperatorExpression(Operator operator) {
        this.operator = operator;
    }
}
