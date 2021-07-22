package lucis.compiler.utility;

import compiler.lexer.LexicalRule;
import compiler.lexer.RegularExpression;

public final class LexicalRules {
    public static final RegularExpression TILDE = RegularExpression.pure("~");
    public static final RegularExpression NONZERO = RegularExpression.range('1', '9' + 1);
    public static final RegularExpression ZERO = RegularExpression.pure("0");
    public static final RegularExpression NEW_LINE = RegularExpression.pure("\n");
    public static final RegularExpression CARRIAGE_RETURN = RegularExpression.pure("\r");
    public static final RegularExpression WHITESPACE = RegularExpression.pure(" ");
    public static final RegularExpression TAB = RegularExpression.pure("\t");
    public static final RegularExpression UPPER_LETTERS = RegularExpression.range('A', 'Z' + 1);
    public static final RegularExpression LOWER_LETTERS = RegularExpression.range('a', 'z' + 1);
    public static final RegularExpression LETTERS = RegularExpression.alternate(UPPER_LETTERS, LOWER_LETTERS);
    public static final RegularExpression DIGITS = RegularExpression.range('0', '9' + 1);

    @LexicalRule("_")
    public static final RegularExpression DISCARD = RegularExpression.pure("_");
    @LexicalRule(".")
    public static final RegularExpression DOT = RegularExpression.pure(".");
    @LexicalRule(",")
    public static final RegularExpression COMMA = RegularExpression.pure(",");
    @LexicalRule(":")
    public static final RegularExpression COLON = RegularExpression.pure(":");
    @LexicalRule(";")
    public static final RegularExpression SEMICOLON = RegularExpression.pure(";");
    @LexicalRule("?")
    public static final RegularExpression QUESTION = RegularExpression.pure("?");
    @LexicalRule("@")
    public static final RegularExpression AT = RegularExpression.pure("@");
    @LexicalRule("#")
    public static final RegularExpression SHARP = RegularExpression.pure("#");
    @LexicalRule("...")
    public static final RegularExpression ELLIPSIS = RegularExpression.pure("...");
    @LexicalRule("=")
    public static final RegularExpression ASSIGN = RegularExpression.pure("=");
    @LexicalRule("+")
    public static final RegularExpression POSITIVE = RegularExpression.pure("+");
    @LexicalRule("-")
    public static final RegularExpression NEGATIVE = RegularExpression.pure("-");
    @LexicalRule("*")
    public static final RegularExpression MULTIPLY = RegularExpression.pure("*");
    @LexicalRule("/")
    public static final RegularExpression DIVISION = RegularExpression.pure("/");
    @LexicalRule("%")
    public static final RegularExpression REMAINDER = RegularExpression.pure("%");
    @LexicalRule("&")
    public static final RegularExpression AND = RegularExpression.pure("&");
    @LexicalRule("|")
    public static final RegularExpression OR = RegularExpression.pure("|");
    @LexicalRule("!")
    public static final RegularExpression NOT = RegularExpression.pure("!");
    @LexicalRule("'")
    public static final RegularExpression SINGLE_QUOTE = RegularExpression.pure("'");
    @LexicalRule("\"")
    public static final RegularExpression DOUBLE_QUOTE = RegularExpression.pure("\"");
    @LexicalRule("`")
    public static final RegularExpression BACK_QUOTE = RegularExpression.pure("`");
    @LexicalRule("(")
    public static final RegularExpression L_ROUND_BRACKET = RegularExpression.pure("(");
    @LexicalRule(")")
    public static final RegularExpression R_ROUND_BRACKET = RegularExpression.pure(")");
    @LexicalRule("[")
    public static final RegularExpression L_SQUARE_BRACKET = RegularExpression.pure("[");
    @LexicalRule("]")
    public static final RegularExpression R_SQUARE_BRACKET = RegularExpression.pure("]");
    @LexicalRule("<")
    public static final RegularExpression L_ANGLE_BRACKET = RegularExpression.pure("<");
    @LexicalRule(">")
    public static final RegularExpression R_ANGLE_BRACKET = RegularExpression.pure(">");
    @LexicalRule("{")
    public static final RegularExpression L_CURLY_BRACKET = RegularExpression.pure("{");
    @LexicalRule("}")
    public static final RegularExpression R_CURLY_BRACKET = RegularExpression.pure("}");

    @LexicalRule("blank")
    public static final RegularExpression BLANK = RegularExpression.alternate(NEW_LINE, CARRIAGE_RETURN, WHITESPACE, TAB).multiple();
    @LexicalRule("in")
    public static final RegularExpression IN = RegularExpression.pure("in");
    @LexicalRule("is")
    public static final RegularExpression IS = RegularExpression.pure("is");
    @LexicalRule("as")
    public static final RegularExpression AS = RegularExpression.pure("as");
    @LexicalRule("if")
    public static final RegularExpression IF = RegularExpression.pure("if");
    @LexicalRule("else")
    public static final RegularExpression ELSE = RegularExpression.pure("else");
    @LexicalRule("when")
    public static final RegularExpression WHEN = RegularExpression.pure("when");
    @LexicalRule("while")
    public static final RegularExpression WHILE = RegularExpression.pure("while");
    @LexicalRule("break")
    public static final RegularExpression BREAK = RegularExpression.pure("break");
    @LexicalRule("class")
    public static final RegularExpression CLASS = RegularExpression.pure("class");
    @LexicalRule("trait")
    public static final RegularExpression TRAIT = RegularExpression.pure("trait");
    @LexicalRule("import")
    public static final RegularExpression IMPORT = RegularExpression.pure("import");
    @LexicalRule("export")
    public static final RegularExpression EXPORT = RegularExpression.pure("export");
    @LexicalRule("lambda")
    public static final RegularExpression LAMBDA = RegularExpression.pure("lambda");
    @LexicalRule("assert")
    public static final RegularExpression ASSERT = RegularExpression.pure("assert");
    @LexicalRule("native")
    public static final RegularExpression NATIVE = RegularExpression.pure("native");
    @LexicalRule("return")
    public static final RegularExpression RETURN = RegularExpression.pure("return");
    @LexicalRule("module")
    public static final RegularExpression MODULE = RegularExpression.pure("module");

    public static final RegularExpression SYMBOL = RegularExpression.concatenate(
            RegularExpression.alternate(LETTERS, DISCARD),
            RegularExpression.alternate(LETTERS, DIGITS, DISCARD).closure()
    );
    @LexicalRule(value = "identifier", priority = -1)
    public static final RegularExpression IDENTIFIER = RegularExpression.concatenate(
            SYMBOL,
            RegularExpression.concatenate(DOT, SYMBOL).closure()
    );

    @LexicalRule("integer")
    public static final RegularExpression INTEGER_LITERAL = RegularExpression.alternate(
            ZERO,
            RegularExpression.concatenate(NONZERO, DIGITS.closure())
    );

    @LexicalRule("decimal")
    public static final RegularExpression DECIMAL_LITERAL = RegularExpression.concatenate(
            INTEGER_LITERAL,
            DOT,
            DIGITS.multiple()
    );

    @LexicalRule("normal-string")
    public static final RegularExpression NORMAL_STRING_LITERAL = RegularExpression.concatenate(
            DOUBLE_QUOTE,
            RegularExpression.alternate(RegularExpression.pure("\\\""), RegularExpression.negate('"')).closure(),
            DOUBLE_QUOTE
    );

    @LexicalRule("raw-string")
    public static final RegularExpression RAW_STRING_LITERAL = RegularExpression.concatenate(
            BACK_QUOTE,
            RegularExpression.alternate(RegularExpression.pure("\\`"), RegularExpression.negate('`')).closure(),
            BACK_QUOTE
    );

    @LexicalRule("block-comment")
    public static final RegularExpression BLOCK_COMMENT = RegularExpression.concatenate(
            TILDE.repeat(2),
            TILDE.optional(),
            RegularExpression.alternate(
                    RegularExpression.concatenate(RegularExpression.negate('~'), TILDE),
                    RegularExpression.negate('~')
            ).closure(),
            TILDE.repeat(2)
    );

    @LexicalRule(value = "line-comment", priority = -1)
    public static final RegularExpression LINE_COMMENT = RegularExpression.concatenate(
            TILDE,
            RegularExpression.negate('\n').closure()
    );

    private LexicalRules() {
    }
}
