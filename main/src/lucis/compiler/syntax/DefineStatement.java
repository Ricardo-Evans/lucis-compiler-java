package lucis.compiler.syntax;

public class DefineStatement extends Statement {
    public final String type;
    public final String identifier;
    public final Expression value;

    public DefineStatement(String type, String identifier, Expression value) {
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitDefineStatement(this);
    }
}
