package lucis.compiler.syntax;

import java.util.List;

public class BlockExpression implements Expression {
    public final List<Statement> statements;

    public BlockExpression(List<Statement> statements) {
        this.statements = statements;
    }
}
