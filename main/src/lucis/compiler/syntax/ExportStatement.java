package lucis.compiler.syntax;

public class ExportStatement extends Statement {
    public final IdentifierExpression expression;

    public ExportStatement(IdentifierExpression expression) {
        this.expression = expression;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitExportStatement(this, data);
    }
}
