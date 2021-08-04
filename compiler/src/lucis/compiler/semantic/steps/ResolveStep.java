package lucis.compiler.semantic.steps;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.semantic.*;
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
                LucisModule module = environment.findModule(i.module()).orElseThrow(() -> new SemanticException("import module " + i.module()+" not found"));
                Set<LucisSymbol> symbols = module.findSymbol(i.name());
                if (symbols.isEmpty()) throw new SemanticException("cannot find " + i);
                context.importSymbols(i.name(), symbols);
            });
        } else if (tree instanceof FunctionStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("cannot define function " + statement.identifier + " outside modules"));
            LucisType result = Utility.uniqueType(statement.type, context, environment);
            LucisType[] parameters = statement.parameters.stream()
                    .map(FunctionStatement.Parameter::type)
                    .map(t -> Utility.uniqueType(t, context, environment))
                    .toArray(LucisType[]::new);
            LucisFunction function = new LucisFunction(statement.identifier, Utility.calculateSignature(result, parameters));
            LucisSymbol symbol = module.foundFunction(function);
            context.foundSymbol(symbol);
        }
        return true;
    }
}
