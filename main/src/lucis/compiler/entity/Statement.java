package lucis.compiler.entity;

import lucis.compiler.utility.Name;

@Name("statement")
public class Statement implements SyntaxTree {
    public final Statement statement;

    public Statement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public Position position() {
        return null;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitStatement(this, data);
    }
}
