package lucis.compiler.syntax;

public class DefineStatement extends Statement {
    public final UniqueIdentifier type;
    public final String identifier;
    public final Expression value;

    public DefineStatement(UniqueIdentifier type, String identifier, Expression value) {
        super(value);
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitDefineStatement(this);
    }
}
