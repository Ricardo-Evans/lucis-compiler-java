package lucis.compiler.syntax;

import java.util.List;

public class FunctionStatement extends Statement {
    public final UniqueIdentifier type;
    public final String identifier;
    public final List<Parameter> parameters;
    public final Statement body;

    public FunctionStatement(UniqueIdentifier type, String identifier, List<Parameter> parameters, Statement body) {
        super(body);
        this.type = type;
        this.identifier = identifier;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitFunctionStatement(this);
    }

    public record Parameter(UniqueIdentifier type, String identifier) {
    }
}
