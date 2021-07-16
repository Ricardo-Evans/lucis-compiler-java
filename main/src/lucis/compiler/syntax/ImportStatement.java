package lucis.compiler.syntax;

public class ImportStatement extends Statement {
    public final String identifier;

    public ImportStatement(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public <T> T visit(Visitor<T> visitor)  {
        return visitor.visitImportStatement(this);
    }
}
