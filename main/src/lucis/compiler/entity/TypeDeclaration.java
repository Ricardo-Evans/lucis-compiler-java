package lucis.compiler.entity;

public interface TypeDeclaration extends DeclarationTree {
    String getName();

    Type resolve();
}
