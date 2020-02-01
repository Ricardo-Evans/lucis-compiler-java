package lucis.compiler.entity;

public interface VariableDeclaration extends DeclarationTree {
    TypeDeclaration getType();

    String getName();

    ExpressionTree getInitializer();
}
