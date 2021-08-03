package lucis.compiler.semantic.steps;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.semantic.Context;
import lucis.compiler.semantic.Environment;
import lucis.compiler.semantic.LucisModule;
import lucis.compiler.semantic.LucisSymbol;
import lucis.compiler.syntax.FunctionStatement;
import lucis.compiler.syntax.ModuleHeader;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;

import java.util.Set;

public class ResolveStep implements Step<SyntaxTree, Environment> {
    @Override
    public boolean process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        if (tree instanceof Source source) {
            ModuleHeader header = source.header;
            header.imports.forEach(i -> {
                LucisModule module = environment.findModule(i.module()).orElseThrow(() -> new SemanticException(""));
                Set<LucisSymbol> symbols = module.findSymbol(i.name());
                if (symbols.isEmpty()) throw new SemanticException("cannot find " + i);
                context.importSymbols(i.name(), symbols);
            });
        } else if (tree instanceof FunctionStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("cannot define function " + statement.identifier + " outside modules"));
            
            LucisSymbol symbol = new LucisSymbol(statement.identifier, module.name, LucisSymbol.Kind.FUNCTION);
        }
        return true;
    }
}
