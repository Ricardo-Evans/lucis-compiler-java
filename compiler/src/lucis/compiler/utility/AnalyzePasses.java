package lucis.compiler.utility;

import compiler.semantic.BFSPass;
import compiler.semantic.DFSPass;
import compiler.semantic.Pass;
import lucis.compiler.semantic.step.*;
import lucis.compiler.semantic.Environment;
import lucis.compiler.syntax.SyntaxTree;

public final class AnalyzePasses {
    public static final Pass<SyntaxTree, Environment> PreparePass = new BFSPass.Builder<SyntaxTree, Environment>()
            .step(new InitializeContextStep())
            .step(new CollectModuleStep())
            .step(new CollectSymbolStep())
            .build();

    public static final Pass<SyntaxTree, Environment> CollectPass = new DFSPass.Builder<SyntaxTree, Environment>()
            .step(new CollectTypeStep())
            .build();

    public static final Pass<SyntaxTree, Environment> ResolvePass = new DFSPass.Builder<SyntaxTree, Environment>()
            .step(new ResolveImportStep())
            .build();

    private AnalyzePasses() {
    }
}
