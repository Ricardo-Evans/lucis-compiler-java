package lucis.compiler.entity;

public interface SyntaxTree {
    String name();

    //<R, D> R visit(Visitor<R, D> visitor, D data);

    interface Visitor<R, D> {
        void visitToken(Lexeme lexeme);
    }
}
