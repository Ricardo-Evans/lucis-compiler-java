package lucis.compiler.syntax;

import java.util.List;

public class FunctionStatement extends Statement {
    public final Symbol type;
    public final String identifier;
    public final List<Parameter> parameters;
    public final Statement body;

    public FunctionStatement(Symbol type, String identifier, List<Parameter> parameters, Statement body) {
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

    public static class Parameter {
        public final Symbol type;
        public final String identifier;

        public Parameter(Symbol type, String identifier) {
            this.type = type;
            this.identifier = identifier;
        }
    }
}
