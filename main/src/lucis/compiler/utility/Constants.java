package lucis.compiler.utility;

import lucis.compiler.ir.LucisType;
import lucis.compiler.lexer.RegularExpression;

import java.util.List;

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
    public static final RegularExpression SHARP = RegularExpression.pure("#");
    public static final RegularExpression TILDE = RegularExpression.pure("~");
    public static final RegularExpression ELLIPSIS = RegularExpression.pure("...");
    public static final RegularExpression ASSIGN = RegularExpression.pure("=");
    public static final RegularExpression POSITIVE = RegularExpression.pure("+");
    public static final RegularExpression NEGATIVE = RegularExpression.pure("-");
    public static final RegularExpression MULTIPLY = RegularExpression.pure("*");
    public static final RegularExpression DIVISION = RegularExpression.pure("/");
    public static final RegularExpression REMAINDER = RegularExpression.pure("%");
    public static final RegularExpression AND = RegularExpression.pure("&");
    public static final RegularExpression OR = RegularExpression.pure("|");
    public static final RegularExpression NOT = RegularExpression.pure("!");

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

    public static final RegularExpression NEW_LINE = RegularExpression.pure("\n");
    public static final RegularExpression CARRIAGE_RETURN = RegularExpression.pure("\r");
    public static final RegularExpression WHITESPACE = RegularExpression.pure(" ");
    public static final RegularExpression TAB = RegularExpression.pure("\t");

    public static final RegularExpression IN = RegularExpression.pure("in");
    public static final RegularExpression IS = RegularExpression.pure("is");
    public static final RegularExpression AS = RegularExpression.pure("as");
    public static final RegularExpression IF = RegularExpression.pure("if");
    public static final RegularExpression ELSE = RegularExpression.pure("else");
    public static final RegularExpression WHEN = RegularExpression.pure("when");
    public static final RegularExpression WHILE = RegularExpression.pure("while");
    public static final RegularExpression BREAK = RegularExpression.pure("break");
    public static final RegularExpression CLASS = RegularExpression.pure("class");
    public static final RegularExpression TRAIT = RegularExpression.pure("trait");
    public static final RegularExpression IMPORT = RegularExpression.pure("import");
    public static final RegularExpression EXPORT = RegularExpression.pure("export");
    public static final RegularExpression LAMBDA = RegularExpression.pure("lambda");
    public static final RegularExpression ASSERT = RegularExpression.pure("assert");
    public static final RegularExpression NATIVE = RegularExpression.pure("native");
    public static final RegularExpression RETURN = RegularExpression.pure("return");

    public static final RegularExpression BLANK = RegularExpression.alternate(NEW_LINE, CARRIAGE_RETURN, WHITESPACE, TAB).multiple();
    public static final RegularExpression SYMBOL = RegularExpression.concatenate(
            RegularExpression.alternate(LETTERS, DISCARD),
            RegularExpression.alternate(LETTERS, DIGITS, DISCARD).closure()
    );
    public static final RegularExpression IDENTIFIER = RegularExpression.concatenate(
            SYMBOL,
            RegularExpression.concatenate(DOT, SYMBOL).closure()
    );
    public static final RegularExpression INTEGER_LITERAL = RegularExpression.alternate(
            ZERO,
            RegularExpression.concatenate(NONZERO, DIGITS.closure())
    );
    public static final RegularExpression DECIMAL_LITERAL = RegularExpression.concatenate(
            INTEGER_LITERAL,
            DOT,
            DIGITS.multiple()
    );
    public static final RegularExpression NORMAL_STRING_LITERAL = RegularExpression.concatenate(
            DOUBLE_QUOTE,
            RegularExpression.alternate(RegularExpression.pure("\\\""), RegularExpression.negate('"')).closure(),
            DOUBLE_QUOTE
    );
    public static final RegularExpression RAW_STRING_LITERAL = RegularExpression.concatenate(
            BACK_QUOTE,
            RegularExpression.alternate(RegularExpression.pure("\\`"), RegularExpression.negate('`')).closure(),
            BACK_QUOTE
    );
    public static final RegularExpression LINE_COMMENT = RegularExpression.concatenate(
            TILDE,
            RegularExpression.negate('\n').closure()
    );
    public static final RegularExpression BLOCK_COMMENT = RegularExpression.concatenate(
            TILDE.repeat(2),
            TILDE.optional(),
            RegularExpression.alternate(
                    RegularExpression.concatenate(RegularExpression.negate('~'), TILDE),
                    RegularExpression.negate('~')
            ).closure(),
            TILDE.repeat(2)
    );

    public static final LucisType OBJECT_TYPE = new LucisType("lucis.core.Object", List.of());
    public static final LucisType TYPE_TYPE = new LucisType("lucis.core.Type");
    public static final LucisType INTEGER_TYPE = new LucisType("lucis.core.Integer");
    public static final LucisType DECIMAL_TYPE = new LucisType("lucis.core.Decimal");
    public static final LucisType STRING_TYPE = new LucisType("lucis.core.String");
    public static final LucisType MODULE_TYPE = new LucisType("lucis.core.Module");
    public static final LucisType FUNCTION_TYPE = new LucisType("lucis.core.Function");
}
