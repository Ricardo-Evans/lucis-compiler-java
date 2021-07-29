package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.syntax.*;

public class CollectSymbolStep implements Step<SyntaxTree> {
    @Override
    public boolean process(SyntaxTree tree) {
        Context context = tree.context();
        if (tree instanceof Source source) {
            ModuleHeader header = source.header;
            context.setCurrentModule(header.name);
        }
        if (tree instanceof ClassStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + statement.name + " is defined outside any module"));
            LucisType type = new LucisType(statement.name, LucisType.Kind.CLASS, module);
            module.foundType(statement.name, type);
        } else if (tree instanceof TraitStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + statement.name + " is defined outside any module"));
            LucisType type = new LucisType(statement.name, LucisType.Kind.TRAIT, module);
            module.foundType(statement.name, type);
        }
        return false;
    }
}
