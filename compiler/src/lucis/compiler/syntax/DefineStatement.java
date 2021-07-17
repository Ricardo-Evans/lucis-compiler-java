package lucis.compiler.syntax;

public class DefineStatement extends Statement {
    public final String type;
    public final String identifier;
    public final Expression value;

    public DefineStatement(String type, String identifier, Expression value) {
        super(value);
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitDefineStatement(this);
    }
}
