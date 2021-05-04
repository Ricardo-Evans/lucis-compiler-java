package lucis.compiler.syntax;

import java.util.List;

public class FunctionStatement extends Statement {
    public final String type;
    public final String identifier;
    public final List<Parameter> parameters;
    public final Expression body;

    public FunctionStatement(String type, String identifier, List<Parameter> parameters, Expression body) {
        this.type = type;
        this.identifier = identifier;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitFunctionStatement(this);
    }

    public static class Parameter {
        public final String type;
        public final String identifier;

        public Parameter(String type, String identifier) {
            this.type = type;
            this.identifier = identifier;
        }
    }
}
