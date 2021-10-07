package lucis.compiler.semantic.step;

import compiler.semantic.Step;
import lucis.compiler.semantic.Context;
import lucis.compiler.semantic.Environment;
import lucis.compiler.syntax.*;

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
