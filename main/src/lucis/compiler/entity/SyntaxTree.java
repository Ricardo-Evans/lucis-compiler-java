package lucis.compiler.entity;

import lucis.compiler.utility.Name;

public interface SyntaxTree {
    default String name() {
        Name name = getClass().getAnnotation(Name.class);
        if (name != null) return name.value();
        else throw new IllegalStateException(getClass() + " has no name");
    }

    Position position();

    <R, D> R visit(Visitor<R, D> visitor, D data);

    interface Visitor<R, D> {
        R visitLexeme(Lexeme lexeme, D data);

        R visitSource(Source source, D data);

        R visitExpression(Expression expression, D data);

        R visitStatement(Statement statement, D data);
    }
}
