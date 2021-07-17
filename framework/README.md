### Lucis Compiler Framework

This project is intended to provide the framework for compiler of lucis language, but can also be used on other
languages with regular lexical rules and context free grammars.

This is only a brief introduction, more information can be found in the javadoc.

This project can be seperated into three parts: Lexers, Parsers and Analyzers.

#### Lexers

Lexical rules can be defined by regular expressions.

By the time, only a few operations of regular expressions are supported:

* Concatenation, Alternation, Closure are the basic operations, which is complete already.

* Optional, Multiple, Repeat are supported for convenience with little effort.

* Empty, Any, Range, Negate are only supported for single character.

DFA(Deterministic Finite Automation) is the only lexer implemented by the time, NFA(Nondeterministic Finite Automation)
may be added in the future with low priority.

Lexers can be build by their builders. The traditional way is passing a RegularExpression, with its lexical name and
priority to Builders. Another more convenient way, define by annotations, is also available. Following is an example(
Lucis Language):

```Java
import compiler.lexer.Lexer;

public final class LexicalRules {
    public static final RegularExpression UPPER_LETTERS = RegularExpression.range('A', 'Z' + 1);
    public static final RegularExpression LOWER_LETTERS = RegularExpression.range('a', 'z' + 1);
    public static final RegularExpression LETTERS = RegularExpression.alternate(UPPER_LETTERS, LOWER_LETTERS);
    public static final RegularExpression DIGITS = RegularExpression.range('0', '9' + 1);

    @LexicalRule("_")
    public static final RegularExpression DISCARD = RegularExpression.pure("_");
    @LexicalRule(".")
    public static final RegularExpression DOT = RegularExpression.pure(".");
    public static final RegularExpression SYMBOL = RegularExpression.concatenate(
            RegularExpression.alternate(LETTERS, DISCARD),
            RegularExpression.alternate(LETTERS, DIGITS, DISCARD).closure()
    );
    @LexicalRule(value = "identifier", priority = -1)
    public static final RegularExpression IDENTIFIER = RegularExpression.concatenate(
            SYMBOL,
            RegularExpression.concatenate(DOT, SYMBOL).closure()
    );

    public static void main(String[] args) {
        Lexer.Builder builder = new DFALexer.Builder();
        builder.define(IDENTIFIER, "identifier", -1); // Define by the traditional way
        builder.define(LexicalRules.class); // Define by annotations
        Lexer lexer = builder.build();
    }
}
```
