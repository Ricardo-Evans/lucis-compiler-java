package lucis.compiler.semantic.steps;

import compiler.semantic.Step;
import lucis.compiler.semantic.Environment;
import lucis.compiler.syntax.FunctionStatement;
import lucis.compiler.syntax.ModuleHeader;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;

public class ResolveStep implements Step<SyntaxTree, Environment> {
    @Override
    public boolean process(SyntaxTree tree, Environment environment) {
        if (tree instanceof Source source) {
            ModuleHeader header = source.header;

        }
        if (tree instanceof FunctionStatement statement) {

        }
        return true;
    }
}
