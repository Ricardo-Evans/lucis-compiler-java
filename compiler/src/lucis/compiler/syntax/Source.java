package lucis.compiler.syntax;

import java.util.List;

public class Source extends SyntaxTree {
    public final ModuleHeader header;
    public final List<Statement> statements;

    public Source(ModuleHeader header, List<Statement> statements) {
        super(statements);
        this.header = header;
        this.statements = statements;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitSource(this);
    }
}
