package lucis.compiler.syntax;

public class ExportStatement extends Statement {
    public final String identifier;

    public ExportStatement(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitExportStatement(this);
    }
}
