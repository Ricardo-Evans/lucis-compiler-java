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
            case ClassStatement statement -> context.requireCurrentModule().foundSymbol(statement.name);
            case FunctionStatement statement -> context.requireCurrentModule().foundSymbol(statement.identifier);
            case TraitStatement statement -> context.requireCurrentModule().foundSymbol(statement.name);
            default -> {
            }
        }
    }
}
