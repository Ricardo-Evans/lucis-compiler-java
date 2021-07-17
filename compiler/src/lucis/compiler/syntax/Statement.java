package lucis.compiler.syntax;

import java.util.List;

public abstract class Statement extends SyntaxTree {
    protected Statement(SyntaxTree... children) {
        super(children);
    }

    protected Statement(List<SyntaxTree> children) {
        super(children);
    }
}
