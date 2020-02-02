package lucis.compiler.entity;

public interface SymbolTable {
    Type getType(String name);

    Variable getVariable(String name);

    Function getFunction(String name);

    void setType(String name, Type type);

    void setVariable(String name, Variable variable);

    void setFunction(String name, Function function);
}
