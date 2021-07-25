package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.ir.LucisType;
import lucis.compiler.syntax.ClassStatement;
import lucis.compiler.syntax.SyntaxTree;
import lucis.compiler.syntax.TraitStatement;

public class CollectSymbolStep implements Step<SyntaxTree> {
    @Override
    public boolean process(SyntaxTree tree) {
        Context context = tree.context();
        if (tree instanceof ClassStatement) {
            String module = context.findModule().orElseThrow(() -> new SemanticException("expect a module statement"));
            String name = ((ClassStatement) tree).name;
            LucisType type = new LucisType(name, module);
            context.foundType(name, type);
        } else if (tree instanceof TraitStatement) {

        }
        return false;
    }
}
