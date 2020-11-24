package lucis.compiler.syntax;

import lucis.compiler.entity.Position;

import java.util.List;

public class Source implements SyntaxTree {
    private final List<Statement> statements;

    public Source(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> statements() {
        return statements;
    }

    @Override
    public Position position() {
        return statements.stream().findAny().map(SyntaxTree::position).orElse(Position.ROOT);
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitSource(this, data);
    }
}