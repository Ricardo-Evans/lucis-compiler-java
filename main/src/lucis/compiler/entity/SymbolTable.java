package lucis.compiler.entity;

public interface SymbolTable {
    TypeDeclaration getType(String name);

    VariableDeclaration getVariable(String name);

    FunctionDeclaration getFunction(String name);

    void setType(String name, TypeDeclaration type);

    void setVariable(String name, VariableDeclaration variable);

    void setFunction(String name, FunctionDeclaration function);
}
