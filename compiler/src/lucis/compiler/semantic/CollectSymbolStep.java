package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.syntax.*;

public class CollectSymbolStep implements Step<SyntaxTree, Environment> {
    @Override
    public boolean process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        if (tree instanceof Source source) {
            ModuleHeader header = source.header;
            LucisModule module = environment.foundModule(header.name);
            context.setCurrentModule(module);
        } else if (tree instanceof ClassStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + statement.name + " is defined outside any fullName"));
            LucisSymbol symbol = new LucisSymbol(statement.name, module.name, LucisSymbol.Kind.TYPE);
            LucisType type = new LucisType(statement.name, LucisType.Kind.CLASS, module);
            module.foundType(symbol, type);
        } else if (tree instanceof TraitStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + statement.name + " is defined outside any fullName"));
            LucisSymbol symbol = new LucisSymbol(statement.name, module.name, LucisSymbol.Kind.TYPE);
            LucisType type = new LucisType(statement.name, LucisType.Kind.TRAIT, module);
            module.foundType(symbol, type);
        }
        return false;
    }
}
