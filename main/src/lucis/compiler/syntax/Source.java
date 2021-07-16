package lucis.compiler.syntax;

import java.util.List;

public class Source extends SyntaxTree {
    public final List<Statement> statements;

    public Source(List<Statement> statements) {
        super(statements.toArray(SyntaxTree[]::new));
        this.statements = statements;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitSource(this);
    }
}
