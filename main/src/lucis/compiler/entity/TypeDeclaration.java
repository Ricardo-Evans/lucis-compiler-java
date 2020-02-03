package lucis.compiler.entity;

import java.util.List;

public interface TypeDeclaration extends DeclarationTree {
    boolean isVirtual();

    TypeDeclaration getBaseClass();

    List<TypeDeclaration> getTraits();

    String getName();

    List<FunctionDeclaration> getMethods();

    List<VariableDeclaration> getFields();
}
