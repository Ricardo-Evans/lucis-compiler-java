package lucis.compiler.entity;

public interface SyntaxTree {
    String tag();

    //<R, D> R visit(Visitor<R, D> visitor, D data);

    interface Visitor<R, D> {
        void visitToken(Token token);
    }
}
