package lucis.compiler.semantic.step;

import compiler.semantic.Step;
import lucis.compiler.semantic.Context;
import lucis.compiler.semantic.Environment;
import lucis.compiler.syntax.*;

import java.util.Objects;

public class InitializeContextStep implements Step<SyntaxTree, Environment> {
    @Override
    public void process(SyntaxTree tree, Environment environment) {
        Context currentContext = tree.context();
        switch (tree) {
            case BlockStatement statement -> {
                Context context = new Context(currentContext);
                statement.statements.forEach(s -> s.context(context));
            }
            case BranchStatement statement -> {
                Context positiveContext = new Context(currentContext);
                Context negativeContext = new Context(currentContext);
                statement.condition.context(currentContext);
                statement.positive.context(positiveContext);
                statement.negative.context(negativeContext);
            }
            case ClassStatement statement -> statement.context(new Context(currentContext));
            case FunctionStatement statement -> statement.body.context(new Context(currentContext));
            case Source source -> {
                source.context(new Context());
                Context context = new Context(source.context());
                source.statements.forEach(s -> s.context(context));
            }
            case TraitStatement statement -> {
                Context context = new Context(currentContext);
                statement.context(context);
                statement.statements.forEach(s -> s.context(context));
                statement.bases.forEach(s -> s.context(currentContext));
            }
            default -> tree.children().stream().filter(Objects::nonNull).forEach(t -> t.context(currentContext));
        }
    }
}
