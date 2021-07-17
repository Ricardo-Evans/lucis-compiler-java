package lucis.compiler.syntax;

import java.util.List;

public abstract class OperatorExpression extends Expression {
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

    protected OperatorExpression(Operator operator, SyntaxTree... children) {
        super(children);
        this.operator = operator;
    }

    protected OperatorExpression(Operator operator, List<SyntaxTree> children) {
        super(children);
        this.operator = operator;
    }
}
