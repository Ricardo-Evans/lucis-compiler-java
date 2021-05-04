package lucis.compiler.syntax;

import java.util.List;

public class FunctionExpression extends Expression {
    public final Expression function;
    public final List<Expression> parameters;

    public FunctionExpression(Expression function, List<Expression> parameters) {
        this.function = function;
        this.parameters = parameters;
    }

    @Override
    public void visit(Visitor visitor)  {
        visitor.visitFunctionExpression(this);
    }
}
