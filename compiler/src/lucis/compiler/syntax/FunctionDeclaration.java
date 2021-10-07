package lucis.compiler.syntax;

import java.util.List;

public class FunctionDeclaration extends Declaration {
    public final UniqueIdentifier type;
    public final String identifier;
    public final List<Parameter> parameters;
    public final Statement body;

    public FunctionDeclaration(UniqueIdentifier type, String identifier, List<Parameter> parameters, Statement body) {
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
