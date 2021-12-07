package lucis.compiler.semantic.analyze;

import lucis.compiler.semantic.AnalyzeStep;
import lucis.compiler.syntax.ModuleHeader;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;

public class CollectModule implements AnalyzeStep {
    @Override
    public void process(SyntaxTree tree, Context context, Environment environment) {
        if (tree instanceof Source source) {
            ModuleHeader header = source.header;
            Module module = environment.foundModule(header.name);
            context.setCurrentModule(module);
        }
    }
}
