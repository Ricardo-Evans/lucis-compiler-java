package lucis.compiler.syntax;

public abstract class Statement extends SyntaxTree {
    protected Statement(SyntaxTree... children) {
        super(children);
    }
}
