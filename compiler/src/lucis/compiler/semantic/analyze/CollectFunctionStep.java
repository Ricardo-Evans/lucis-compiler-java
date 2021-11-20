package lucis.compiler.semantic.analyze;

import compiler.semantic.Step;
import lucis.compiler.syntax.FunctionDeclaration;
import lucis.compiler.syntax.SyntaxTree;

public class CollectFunctionStep implements Step<SyntaxTree, Environment> {
    @Override
    public void process(SyntaxTree tree, Environment environment) {
        if (tree instanceof (FunctionDeclaration declaration)) {
            Context context = tree.context();
        }
    }
}
