package lucis.compiler.entity;

public interface VariableDeclaration extends DeclarationTree {
    String getName();

    ExpressionTree getInitializer();

    Variable resolve();
}
