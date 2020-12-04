package lucis.compiler.syntax;

import java.util.List;

public class FunctionStatement extends Statement {
    public final Expression type;
    public final String identifier;
    public final List<Parameter> parameters;
    public final Expression body;

    public FunctionStatement(Expression type, String identifier, List<Parameter> parameters, Expression body) {
        this.type = type;
        this.identifier = identifier;
        this.parameters = parameters;
        this.body = body;
    }

    public static class Parameter extends SyntaxTree {
        public final Expression type;
        public final String identifier;

        public Parameter(Expression type, String identifier) {
            this.type = type;
            this.identifier = identifier;
        }
    }
}
