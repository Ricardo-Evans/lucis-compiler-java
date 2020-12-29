package lucis.compiler.syntax;

public class ImportStatement extends Statement {
    public final IdentifierExpression expression;

    public ImportStatement(IdentifierExpression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitImportStatement(this, data);
    }
}
