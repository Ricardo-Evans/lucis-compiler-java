package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.AnalyzeStep;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;

public class BuiltinType implements AnalyzeStep {
    @Override
    public void process(SyntaxTree tree, Context context, Environment environment) {
        if (tree instanceof Source) {
            Module builtinModule = environment.findModule("lucis.core").orElseThrow(() -> new SemanticException("")); // TODO
            SymbolTable symbolTable = context.getSymbolTable();
            builtinModule.symbols().forEach((name, symbol) -> {
                symbolTable.foundSymbol(name).foundElement(symbol.candidates());
                symbolTable.foundSymbol(builtinModule.name() + ":" + name).foundElement(symbol.candidates());
            });
        }
    }
}
