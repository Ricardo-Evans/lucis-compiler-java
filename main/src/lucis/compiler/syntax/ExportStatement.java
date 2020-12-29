package lucis.compiler.syntax;

public class ExportStatement extends Statement {
    public final IdentifierExpression expression;

    public ExportStatement(IdentifierExpression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitExportStatement(this, data);
    }
}
