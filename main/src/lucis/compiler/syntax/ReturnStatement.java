package lucis.compiler.syntax;

public class ReturnStatement implements SyntaxTree {
    public Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }
}
