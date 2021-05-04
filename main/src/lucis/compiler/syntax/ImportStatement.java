package lucis.compiler.syntax;

public class ImportStatement extends Statement {
    public final String identifier;

    public ImportStatement(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitImportStatement(this);
    }
}
