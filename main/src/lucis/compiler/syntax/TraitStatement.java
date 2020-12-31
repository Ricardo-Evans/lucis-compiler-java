package lucis.compiler.syntax;

import java.util.List;

public class TraitStatement extends Statement {
    public final String name;
    public final List<ElementExpression> bases;
    public final List<FunctionStatement> statements;

    public TraitStatement(String name, List<ElementExpression> bases, List<FunctionStatement> statements) {
        this.name = name;
        this.bases = bases;
        this.statements = statements;
    }

    @Override
    public <T> T visit(Visitor<T> visitor, T data) {
        return visitor.visitTraitStatement(this, data);
    }
}
