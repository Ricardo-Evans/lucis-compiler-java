package lucis.compiler.syntax;

import java.util.List;

public class ClassStatement extends Statement {
    public final String name;
    public final List<ElementExpression> bases;

    public ClassStatement(String name, List<ElementExpression> bases) {
        this.name = name;
        this.bases = bases;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitClassStatement(this, data);
    }
}
