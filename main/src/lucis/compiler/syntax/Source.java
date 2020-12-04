package lucis.compiler.syntax;

import java.util.List;

public class Source extends SyntaxTree {
    public final List<Statement> statements;

    public Source(List<Statement> statements) {
        this.statements = statements;
    }
}
