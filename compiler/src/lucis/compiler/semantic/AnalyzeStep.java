package lucis.compiler.semantic;

import compiler.semantic.Step;
import lucis.compiler.semantic.analyze.Context;
import lucis.compiler.semantic.analyze.Environment;
import lucis.compiler.syntax.SyntaxTree;

public interface AnalyzeStep extends Step<SyntaxTree, Context, Environment> {
}
