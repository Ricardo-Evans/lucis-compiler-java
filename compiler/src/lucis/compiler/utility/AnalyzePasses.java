package lucis.compiler.utility;

import compiler.semantic.BFSPass;
import compiler.semantic.Pass;
import lucis.compiler.semantic.CollectSymbolStep;
import lucis.compiler.semantic.Environment;
import lucis.compiler.semantic.InitializeContextStep;
import lucis.compiler.syntax.SyntaxTree;

public final class AnalyzePasses {
    public static final Pass<SyntaxTree, Environment> CollectPass = new BFSPass.Builder<SyntaxTree, Environment>()
            .step(new InitializeContextStep())
            .step(new CollectSymbolStep())
            .build();

    private AnalyzePasses() {
    }
}
