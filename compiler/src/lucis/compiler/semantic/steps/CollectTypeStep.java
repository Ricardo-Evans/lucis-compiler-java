package lucis.compiler.semantic.steps;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.semantic.*;
import lucis.compiler.syntax.*;

public class CollectTypeStep implements Step<SyntaxTree, Environment> {
    @Override
    public boolean process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        if (tree instanceof ClassStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + statement.name + " is defined outside any module"));
            LucisSymbol symbol = new LucisSymbol(statement.name, module.name, LucisSymbol.Kind.TYPE);
            LucisType type = new LucisType(statement.name, LucisType.Kind.CLASS, module);
            module.foundType(symbol, type);
            context.foundSymbol(symbol);
        } else if (tree instanceof TraitStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + statement.name + " is defined outside any module"));
            LucisSymbol symbol = new LucisSymbol(statement.name, module.name, LucisSymbol.Kind.TYPE);
            LucisType type = new LucisType(statement.name, LucisType.Kind.TRAIT, module);
            module.foundType(symbol, type);
            context.foundSymbol(symbol);
        }
        return false;
    }
}
