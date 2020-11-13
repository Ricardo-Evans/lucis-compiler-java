package lucis.compiler.entity;

import lucis.compiler.lexer.RegularExpression;

public final class Constants {
    private Constants() {
    }

    public static final RegularExpression DISCARD = RegularExpression.pure("_");
    public static final RegularExpression DOT = RegularExpression.pure(".");
    public static final RegularExpression COMMA = RegularExpression.pure(",");
    public static final RegularExpression COLON = RegularExpression.pure(":");
    public static final RegularExpression SEMICOLON = RegularExpression.pure(";");
    public static final RegularExpression QUESTION = RegularExpression.pure("?");
    public static final RegularExpression AT = RegularExpression.pure("@");
    public static final RegularExpression ELLIPSIS = RegularExpression.pure("...");
    public static final RegularExpression ASSIGN = RegularExpression.pure("=");
    public static final RegularExpression POSITIVE = RegularExpression.pure("+");
    public static final RegularExpression NEGATIVE = RegularExpression.pure("-");
    public static final RegularExpression MULTIPLY = RegularExpression.pure("*");
    public static final RegularExpression DIVISION = RegularExpression.pure("/");

    public static final RegularExpression SINGLE_QUOTE = RegularExpression.pure("'");
    public static final RegularExpression DOUBLE_QUOTE = RegularExpression.pure("\"");
    public static final RegularExpression BACK_QUOTE = RegularExpression.pure("`");

    public static final RegularExpression L_ROUND_BRACKET = RegularExpression.pure("(");
    public static final RegularExpression R_ROUND_BRACKET = RegularExpression.pure(")");
    public static final RegularExpression L_SQUARE_BRACKET = RegularExpression.pure("[");
    public static final RegularExpression R_SQUARE_BRACKET = RegularExpression.pure("]");
    public static final RegularExpression L_ANGLE_BRACKET = RegularExpression.pure("<");
    public static final RegularExpression R_ANGLE_BRACKET = RegularExpression.pure(">");
    public static final RegularExpression L_CURLY_BRACKET = RegularExpression.pure("{");
    public static final RegularExpression R_CURLY_BRACKET = RegularExpression.pure("}");

    public static final RegularExpression UPPER_LETTERS = RegularExpression.range('A', 'Z' + 1);
    public static final RegularExpression LOWER_LETTERS = RegularExpression.range('a', 'z' + 1);
    public static final RegularExpression LETTERS = RegularExpression.alternate(UPPER_LETTERS, LOWER_LETTERS);
    public static final RegularExpression DIGITS = RegularExpression.range('0', '9' + 1);
    public static final RegularExpression NONZERO = RegularExpression.range('1', '9' + 1);
    public static final RegularExpression ZERO = RegularExpression.pure("0");

    public static final RegularExpression NEWLINE = RegularExpression.pure("\n");
    public static final RegularExpression WHITESPACE = RegularExpression.pure(" ");
    public static final RegularExpression TAB = RegularExpression.pure("\t");

    public static final RegularExpression IF_KEYWORD = RegularExpression.pure("if");
    public static final RegularExpression BLANK = RegularExpression.alternate(NEWLINE, WHITESPACE, TAB).multiple();
    public static final RegularExpression IDENTIFIER = RegularExpression.concatenate(
            RegularExpression.alternate(LETTERS, DISCARD),
            RegularExpression.alternate(LETTERS, DIGITS, DISCARD).closure()
    );
    public static final RegularExpression INTEGER_LITERAL = RegularExpression.concatenate(
            RegularExpression.alternate(POSITIVE, NEGATIVE).optional(),
            RegularExpression.alternate(
                    ZERO,
                    RegularExpression.concatenate(NONZERO, DIGITS.closure())
            )
    );
    public static final RegularExpression DECIMAL_LITERAL = RegularExpression.concatenate(
            INTEGER_LITERAL,
            DOT,
            DIGITS.multiple()
    );
    public static final RegularExpression STRING_LITERAL = RegularExpression.concatenate(
            DOUBLE_QUOTE,
            RegularExpression.alternate(RegularExpression.pure("\\\""), RegularExpression.negate('"')).closure(),
            DOUBLE_QUOTE
    );
}
