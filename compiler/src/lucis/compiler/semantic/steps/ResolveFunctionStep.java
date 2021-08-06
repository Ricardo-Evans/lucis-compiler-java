package lucis.compiler.semantic.steps;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.semantic.*;
import lucis.compiler.syntax.Expression;
import lucis.compiler.syntax.FunctionStatement;
import lucis.compiler.syntax.Statement;
import lucis.compiler.syntax.SyntaxTree;

public class ResolveFunctionStep implements Step<SyntaxTree, Environment> {
    @Override
    public boolean process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        if (tree instanceof FunctionStatement statement) {
            LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("cannot define function " + statement.identifier + " outside modules"));
            LucisType result = Utility.uniqueType(statement.type, context, environment);
            LucisType[] parameters = statement.parameters.stream()
                    .map(FunctionStatement.Parameter::type)
                    .map(t -> Utility.uniqueType(t, context, environment))
                    .toArray(LucisType[]::new);
            LucisFunction function = new LucisFunction(statement.identifier, Utility.calculateSignature(result, parameters));
            module.foundFunction(function);
        }
        return !(tree instanceof Statement || tree instanceof Expression);
    }
}
