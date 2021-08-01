package lucis.compiler.syntax;

import java.util.List;

public class FunctionStatement extends Statement {
    public final NestedIdentifier type;
    public final String identifier;
    public final List<Parameter> parameters;
    public final Statement body;

    public FunctionStatement(NestedIdentifier type, String identifier, List<Parameter> parameters, Statement body) {
        super(body);
        this.type = type;
        this.identifier = identifier;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitFunctionStatement(this);
    }

    public record Parameter(NestedIdentifier type, String identifier) {
    }
}
