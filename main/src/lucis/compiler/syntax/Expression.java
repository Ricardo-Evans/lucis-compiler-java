package lucis.compiler.syntax;

public abstract class Expression extends SyntaxTree {
    protected Expression(SyntaxTree... children) {
        super(children);
    }
}
