package lucis.compiler.utility;

import compiler.semantic.BFSPass;
import compiler.semantic.Pass;
import lucis.compiler.semantic.InitializeContextStep;
import lucis.compiler.syntax.SyntaxTree;

public class AnalyzePasses {
    public static final Pass<SyntaxTree> CollectPass = new BFSPass.Builder<SyntaxTree>()
            .step(new InitializeContextStep())
            .build();
}
