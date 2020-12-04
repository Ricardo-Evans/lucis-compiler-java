package lucis.compiler.syntax;

public class ReturnStatement extends Statement {
    public Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }
}
