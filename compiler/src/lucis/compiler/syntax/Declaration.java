package lucis.compiler.syntax;

import java.util.List;

public abstract class Declaration extends SyntaxTree {
    protected Declaration(SyntaxTree... children) {
        super(children);
    }

    protected Declaration(List<SyntaxTree> children) {
        super(children);
    }
}
