package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.syntax.*;

public class CollectSymbolStep implements Step<SyntaxTree> {
    @Override
    public boolean process(SyntaxTree tree) {
        Context context = tree.context();
        if (tree instanceof Source) {
            ModuleHeader header = ((Source) tree).header;
            context.foundModule(header.name);
        }
        if (tree instanceof ClassStatement) {
            Symbol module = context.findModule().orElseThrow(() -> new SemanticException("expect a module statement"));
            String name = ((ClassStatement) tree).name;
        } else if (tree instanceof TraitStatement) {

        }
        return false;
    }
}
