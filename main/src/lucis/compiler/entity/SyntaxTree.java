package lucis.compiler.entity;

public interface SyntaxTree {
    String name();

    Position position();

    <R, D> R visit(Visitor<R, D> visitor, D data);

    interface Visitor<R, D> {
        R visitLexeme(Lexeme lexeme, D data);

        R visitSource(Source source, D data);

        R visitExpression(Expression expression, D data);

        R visitStatement(Statement statement, D data);
    }
}
