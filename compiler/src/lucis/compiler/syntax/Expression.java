package lucis.compiler.syntax;

import java.util.List;

public abstract class Expression extends SyntaxTree {
    protected Expression(SyntaxTree... children) {
        super(children);
    }

    protected Expression(List<SyntaxTree> children) {
        super(children);
    }
}
