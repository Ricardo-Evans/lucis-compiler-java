package lucis.compiler.semantic.analyze;

import lucis.compiler.semantic.AnalyzeStep;
import lucis.compiler.syntax.*;

import java.util.Objects;

public class InitializeSymbolTable implements AnalyzeStep {
    @Override
    public void process(SyntaxTree tree, Context context, Environment environment) {
        switch (tree) {
            case null -> {
            }
            case BlockStatement statement -> {
                SymbolTable symbolTable = new SymbolTable(context.getSymbolTable());
                statement.statements.stream().map(SyntaxTree::context).forEach(c -> c.setSymbolTable(symbolTable));
            }
            case BranchStatement statement -> {
                SymbolTable current = context.getSymbolTable();
                SymbolTable positive = new SymbolTable(current);
                SymbolTable negative = new SymbolTable(current);
                statement.condition.context().setSymbolTable(current);
                if (statement.positive != null)
                    statement.positive.context().setSymbolTable(positive);
                if (statement.negative != null)
                    statement.negative.context().setSymbolTable(negative);
            }
            case ClassDeclaration ignored -> context.setSymbolTable(new SymbolTable(context.getSymbolTable()));
            case FunctionDeclaration statement -> statement.body.context().setSymbolTable(new SymbolTable(context.getSymbolTable()));
            case Source source -> {
                context.setSymbolTable(new SymbolTable());
                SymbolTable symbolTable = new SymbolTable(context.getSymbolTable());
                source.statements.stream().map(SyntaxTree::context).forEach(c -> c.setSymbolTable(symbolTable));
            }
            case TraitDeclaration ignored -> context.setSymbolTable(new SymbolTable(context.getSymbolTable()));
            default -> tree.children().stream().filter(Objects::nonNull).map(SyntaxTree::context).forEach(c -> c.setSymbolTable(context.getSymbolTable()));
        }
    }
}
