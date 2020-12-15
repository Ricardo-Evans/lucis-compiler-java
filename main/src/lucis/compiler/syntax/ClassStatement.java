package lucis.compiler.syntax;

import java.util.List;

public class ClassStatement extends Statement {
    public final String name;
    public final List<IdentifierExpression> bases;

    public ClassStatement(String name, List<IdentifierExpression> bases) {
        this.name = name;
        this.bases = bases;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitClassStatement(this, data);
    }
}
