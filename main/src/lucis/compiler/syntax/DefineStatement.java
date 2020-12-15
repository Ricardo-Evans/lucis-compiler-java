package lucis.compiler.syntax;

public class DefineStatement extends Statement {
    public final Expression type;
    public final String identifier;
    public final Expression value;

    public DefineStatement(Expression type, String identifier, Expression value) {
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitDefineStatement(this, data);
    }
}
