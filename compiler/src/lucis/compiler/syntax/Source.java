package lucis.compiler.syntax;

import java.util.List;

public class Source extends SyntaxTree {
    public final ModuleHeader header;
    public final List<Declaration> statements;

    public Source(ModuleHeader header, List<Declaration> declarations) {
        super(declarations);
        this.header = header;
        this.statements = declarations;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitSource(this);
    }
}
