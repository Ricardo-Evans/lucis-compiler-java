package lucis.compiler.semantic.analyze;

import compiler.semantic.Step;
import lucis.compiler.syntax.ClassDeclaration;
import lucis.compiler.syntax.FunctionDeclaration;
import lucis.compiler.syntax.SyntaxTree;
import lucis.compiler.syntax.TraitDeclaration;

public class CollectSymbolStep implements Step<SyntaxTree, Environment> {
    @Override
    public void process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        switch (tree) {
            case ClassDeclaration declaration -> context.requireCurrentModule().foundSymbol(declaration.name);
            case TraitDeclaration declaration -> context.requireCurrentModule().foundSymbol(declaration.name);
            case FunctionDeclaration declaration -> context.requireCurrentModule().foundSymbol(declaration.identifier);
            default -> {
            }
        }
    }
}
