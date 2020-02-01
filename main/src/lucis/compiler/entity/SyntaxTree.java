package lucis.compiler.entity;

public interface SyntaxTree {
    SymbolTable getSymbolTable();

    <R, D> R visit(Visitor<R, D> visitor, D data);

    interface Visitor<R, D> {
        R visitSource(SourceTree that, D data);
    }
}
