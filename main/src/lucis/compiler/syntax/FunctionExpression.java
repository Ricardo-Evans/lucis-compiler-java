package lucis.compiler.syntax;

import java.util.List;
import java.util.stream.Stream;

public class FunctionExpression extends Expression {
    public final Expression function;
    public final List<Expression> parameters;

    public FunctionExpression(Expression function, List<Expression> parameters) {
        super(Stream.concat(Stream.of(function), parameters.stream()).toArray(SyntaxTree[]::new));
        this.function = function;
        this.parameters = parameters;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitFunctionExpression(this);
    }
}
