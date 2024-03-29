package lucis.compiler.utility;

import compiler.semantic.BFSPass;
import compiler.semantic.DFSPass;
import compiler.semantic.Pass;
import lucis.compiler.semantic.steps.CollectModuleStep;
import lucis.compiler.semantic.steps.CollectTypeStep;
import lucis.compiler.semantic.Environment;
import lucis.compiler.semantic.steps.InitializeContextStep;
import lucis.compiler.semantic.steps.ResolveFunctionStep;
import lucis.compiler.syntax.SyntaxTree;

public final class AnalyzePasses {
    public static final Pass<SyntaxTree, Environment> CollectPass = new BFSPass.Builder<SyntaxTree, Environment>()
            .step(new InitializeContextStep())
            .step(new CollectModuleStep())
            .step(new CollectTypeStep())
            .build();

    public static final Pass<SyntaxTree, Environment> ResolvePass = new DFSPass.Builder<SyntaxTree, Environment>()
            .step(new ResolveFunctionStep())
            .build();

    private AnalyzePasses() {
    }
}
