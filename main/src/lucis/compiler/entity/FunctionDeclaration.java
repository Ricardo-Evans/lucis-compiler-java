package lucis.compiler.entity;

import java.util.List;

public interface FunctionDeclaration extends DeclarationTree {
    String getName();

    TypeDeclaration getResultType();

    List<TypeDeclaration> getParameterTypes();
}
