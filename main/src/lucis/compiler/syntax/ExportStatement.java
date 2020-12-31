package lucis.compiler.syntax;

public class ExportStatement extends Statement {
    public final String identifier;

    public ExportStatement(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitExportStatement(this, data);
    }
}
