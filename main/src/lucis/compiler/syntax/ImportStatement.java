package lucis.compiler.syntax;

public class ImportStatement extends Statement {
    public final IdentifierExpression expression;

    public ImportStatement(IdentifierExpression expression) {
        this.expression = expression;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitImportStatement(this, data);
    }
}
