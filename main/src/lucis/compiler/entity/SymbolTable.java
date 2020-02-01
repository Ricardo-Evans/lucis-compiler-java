package lucis.compiler.entity;

public interface SymbolTable {
    TypeDeclaration getType(String name);

    VariableDeclaration getVariable(String name);

    FunctionDeclaration getFunction(String name);
}
