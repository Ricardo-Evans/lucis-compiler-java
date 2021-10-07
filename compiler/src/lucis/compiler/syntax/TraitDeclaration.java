package lucis.compiler.syntax;

import java.util.List;
import java.util.stream.Stream;

public class TraitDeclaration extends Declaration {
    public final String name;
    public final List<ElementExpression> bases;
    public final List<FunctionDeclaration> statements;

    public TraitDeclaration(String name, List<ElementExpression> bases, List<FunctionDeclaration> statements) {
        super(Stream.concat(bases.stream(), statements.stream()).toArray(SyntaxTree[]::new));
        this.name = name;
        this.bases = bases;
        this.statements = statements;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitTraitStatement(this);
    }
}
