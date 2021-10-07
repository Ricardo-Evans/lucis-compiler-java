package lucis.compiler.semantic.step;

import compiler.semantic.Step;
import lucis.compiler.semantic.Context;
import lucis.compiler.semantic.Environment;
import lucis.compiler.syntax.FunctionDeclaration;
import lucis.compiler.syntax.SyntaxTree;

public class CollectFunctionStep implements Step<SyntaxTree, Environment> {
    @Override
    public void process(SyntaxTree tree, Environment environment) {
        if (tree instanceof (FunctionDeclaration statement)) {
            Context context = tree.context();
        }
    }
}
