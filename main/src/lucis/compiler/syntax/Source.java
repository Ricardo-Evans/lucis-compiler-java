package lucis.compiler.syntax;

import java.util.List;

public class Source implements SyntaxTree {
    public final List<Declaration> declarations;

    public Source(List<Declaration> declarations) {
        this.declarations = declarations;
    }
}
