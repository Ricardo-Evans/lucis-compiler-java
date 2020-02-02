package lucis.compiler.entity;

public interface FunctionDeclaration extends DeclarationTree {
    String getName();

    Function resolve();
}
