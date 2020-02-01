package lucis.compiler.entity;

import java.util.List;

public interface SourceTree extends SyntaxTree {
    ExportDeclaration getExport();

    List<ImportDeclaration> getImports();

    List<StatementTree> getStatements();
}
