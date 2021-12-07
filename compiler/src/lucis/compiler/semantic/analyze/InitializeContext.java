package lucis.compiler.semantic.analyze;

import compiler.semantic.Step;
import lucis.compiler.semantic.AnalyzeStep;
import lucis.compiler.syntax.SyntaxTree;

public class InitializeContext implements AnalyzeStep {
    @Override
    public void process(SyntaxTree tree, Context context, Environment environment) {
        tree.context(new Context());
    }
}
