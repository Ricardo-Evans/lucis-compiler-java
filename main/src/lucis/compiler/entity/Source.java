package lucis.compiler.entity;

import java.util.List;

public class Source implements SyntaxTree {
    private final List<Statement> statements;

    public Source(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public String name() {
        return "source";
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitSource(this, data);
    }
}
