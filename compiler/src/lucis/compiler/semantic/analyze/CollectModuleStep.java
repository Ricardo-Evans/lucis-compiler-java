package lucis.compiler.semantic.analyze;

import compiler.semantic.Step;
import lucis.compiler.syntax.ModuleHeader;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;

public class CollectModuleStep implements Step<SyntaxTree, Environment> {
    @Override
    public void process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        if (tree instanceof Source source) {
            ModuleHeader header = source.header;
            Module module = environment.foundModule(header.name);
            context.setCurrentModule(module);
        }
    }
}
