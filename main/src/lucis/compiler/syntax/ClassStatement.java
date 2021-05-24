package lucis.compiler.syntax;

import java.util.List;

public class ClassStatement extends Statement {
    public String name;
    public List<ElementExpression> bases;

    public ClassStatement(String name, List<ElementExpression> bases) {
        super(bases.toArray(ElementExpression[]::new));
        this.name = name;
        this.bases = bases;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitClassStatement(this);
    }
}
