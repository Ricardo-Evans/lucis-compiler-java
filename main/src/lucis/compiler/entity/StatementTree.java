package lucis.compiler.entity;

public class StatementTree implements SyntaxTree {
    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return null;
    }
}
