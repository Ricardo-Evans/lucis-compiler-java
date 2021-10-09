package lucis.compiler.utility;

import compiler.lexer.LexicalRule;
import compiler.lexer.RegularExpression;

public final class LexicalRules {
    private static final RegularExpression TILDE = RegularExpression.pure("~");
    private static final RegularExpression NONZERO = RegularExpression.range('1', '9' + 1);
    private static final RegularExpression ZERO = RegularExpression.pure("0");
    private static final RegularExpression NEW_LINE = RegularExpression.pure("\n");
    private static final RegularExpression CARRIAGE_RETURN = RegularExpression.pure("\r");
    private static final RegularExpression WHITESPACE = RegularExpression.pure(" ");
    private static final RegularExpression TAB = RegularExpression.pure("\t");
    private static final RegularExpression UPPER_LETTERS = RegularExpression.range('A', 'Z' + 1);
    private static final RegularExpression LOWER_LETTERS = RegularExpression.range('a', 'z' + 1);
    private static final RegularExpression LETTERS = RegularExpression.alternate(UPPER_LETTERS, LOWER_LETTERS);
    private static final RegularExpression DIGITS = RegularExpression.range('0', '9' + 1);

    @LexicalRule("_")
    private static final RegularExpression DISCARD = RegularExpression.pure("_");
    @LexicalRule(".")
    private static final RegularExpression DOT = RegularExpression.pure(".");
    @LexicalRule(",")
    private static final RegularExpression COMMA = RegularExpression.pure(",");
    @LexicalRule(":")
    private static final RegularExpression COLON = RegularExpression.pure(":");
    @LexicalRule(";")
    private static final RegularExpression SEMICOLON = RegularExpression.pure(";");
    @LexicalRule("?")
    private static final RegularExpression QUESTION = RegularExpression.pure("?");
    @LexicalRule("@")
    private static final RegularExpression AT = RegularExpression.pure("@");
    @LexicalRule("#")
    private static final RegularExpression SHARP = RegularExpression.pure("#");
    @LexicalRule("...")
    private static final RegularExpression ELLIPSIS = RegularExpression.pure("...");
    @LexicalRule("=")
    private static final RegularExpression ASSIGN = RegularExpression.pure("=");
    @LexicalRule("+")
    private static final RegularExpression POSITIVE = RegularExpression.pure("+");
    @LexicalRule("-")
    private static final RegularExpression NEGATIVE = RegularExpression.pure("-");
    @LexicalRule("*")
    private static final RegularExpression MULTIPLY = RegularExpression.pure("*");
    @LexicalRule("/")
    private static final RegularExpression DIVISION = RegularExpression.pure("/");
    @LexicalRule("%")
    private static final RegularExpression REMAINDER = RegularExpression.pure("%");
    @LexicalRule("&")
    private static final RegularExpression AND = RegularExpression.pure("&");
    @LexicalRule("|")
    private static final RegularExpression OR = RegularExpression.pure("|");
    @LexicalRule("!")
    private static final RegularExpression NOT = RegularExpression.pure("!");
    @LexicalRule("'")
    private static final RegularExpression SINGLE_QUOTE = RegularExpression.pure("'");
    @LexicalRule("\"")
    private static final RegularExpression DOUBLE_QUOTE = RegularExpression.pure("\"");
    @LexicalRule("`")
    private static final RegularExpression BACK_QUOTE = RegularExpression.pure("`");
    @LexicalRule("(")
    private static final RegularExpression L_ROUND_BRACKET = RegularExpression.pure("(");
    @LexicalRule(")")
    private static final RegularExpression R_ROUND_BRACKET = RegularExpression.pure(")");
    @LexicalRule("[")
    private static final RegularExpression L_SQUARE_BRACKET = RegularExpression.pure("[");
    @LexicalRule("]")
    private static final RegularExpression R_SQUARE_BRACKET = RegularExpression.pure("]");
    @LexicalRule("<")
    private static final RegularExpression L_ANGLE_BRACKET = RegularExpression.pure("<");
    @LexicalRule(">")
    private static final RegularExpression R_ANGLE_BRACKET = RegularExpression.pure(">");
    @LexicalRule("{")
    private static final RegularExpression L_CURLY_BRACKET = RegularExpression.pure("{");
    @LexicalRule("}")
    private static final RegularExpression R_CURLY_BRACKET = RegularExpression.pure("}");

    @LexicalRule("blank")
    private static final RegularExpression BLANK = RegularExpression.alternate(NEW_LINE, CARRIAGE_RETURN, WHITESPACE, TAB).multiple();
    @LexicalRule("as")
    private static final RegularExpression AS = RegularExpression.pure("as");
    @LexicalRule("if")
    private static final RegularExpression IF = RegularExpression.pure("if");
    @LexicalRule("in")
    private static final RegularExpression IN = RegularExpression.pure("in");
    @LexicalRule("is")
    private static final RegularExpression IS = RegularExpression.pure("is");
    @LexicalRule("else")
    private static final RegularExpression ELSE = RegularExpression.pure("else");
    @LexicalRule("when")
    private static final RegularExpression WHEN = RegularExpression.pure("when");
    @LexicalRule("break")
    private static final RegularExpression BREAK = RegularExpression.pure("break");
    @LexicalRule("class")
    private static final RegularExpression CLASS = RegularExpression.pure("class");
    @LexicalRule("trait")
    private static final RegularExpression TRAIT = RegularExpression.pure("trait");
    @LexicalRule("while")
    private static final RegularExpression WHILE = RegularExpression.pure("while");
    @LexicalRule("assert")
    private static final RegularExpression ASSERT = RegularExpression.pure("assert");
    @LexicalRule("export")
    private static final RegularExpression EXPORT = RegularExpression.pure("export");
    @LexicalRule("import")
    private static final RegularExpression IMPORT = RegularExpression.pure("import");
    @LexicalRule("lambda")
    private static final RegularExpression LAMBDA = RegularExpression.pure("lambda");
    @LexicalRule("module")
    private static final RegularExpression MODULE = RegularExpression.pure("module");
    @LexicalRule("native")
    private static final RegularExpression NATIVE = RegularExpression.pure("native");
    @LexicalRule("return")
    private static final RegularExpression RETURN = RegularExpression.pure("return");


    @LexicalRule(value = "identifier", priority = -1)
    private static final RegularExpression IDENTIFIER = RegularExpression.concatenate(
            LETTERS,
            RegularExpression.alternate(LETTERS, DIGITS, DISCARD).closure()
    );

    @LexicalRule("integer")
    private static final RegularExpression INTEGER_LITERAL = RegularExpression.alternate(
            ZERO,
            RegularExpression.concatenate(NONZERO, DIGITS.closure())
    );

    @LexicalRule("decimal")
    private static final RegularExpression DECIMAL_LITERAL = RegularExpression.concatenate(
            INTEGER_LITERAL,
            DOT,
            DIGITS.multiple()
    );

    @LexicalRule("normal-string")
    private static final RegularExpression NORMAL_STRING_LITERAL = RegularExpression.concatenate(
            DOUBLE_QUOTE,
            RegularExpression.alternate(RegularExpression.pure("\\\""), RegularExpression.negate('"')).closure(),
            DOUBLE_QUOTE
    );

    @LexicalRule("raw-string")
    private static final RegularExpression RAW_STRING_LITERAL = RegularExpression.concatenate(
            BACK_QUOTE,
            RegularExpression.alternate(RegularExpression.pure("\\`"), RegularExpression.negate('`')).closure(),
            BACK_QUOTE
    );

    @LexicalRule("block-comment")
    private static final RegularExpression BLOCK_COMMENT = RegularExpression.concatenate(
            TILDE.repeat(2),
            TILDE.optional(),
            RegularExpression.alternate(
                    RegularExpression.concatenate(RegularExpression.negate('~'), TILDE),
                    RegularExpression.negate('~')
            ).closure(),
            TILDE.repeat(2)
    );

    @LexicalRule(value = "line-comment", priority = -1)
    private static final RegularExpression LINE_COMMENT = RegularExpression.concatenate(
            TILDE,
            RegularExpression.negate('\n').closure()
    );

    private LexicalRules() {
    }
}
