package lucis.compiler.semantic.analyze;

import compiler.semantic.DefinePass;
import lucis.compiler.syntax.*;

import java.util.Objects;

public final class InitializePasses {
    private InitializePasses() {
    }

    /**
     * Simply initialize context for each syntax tree recursively, necessary for other analysis
     */
    @DefinePass
    public static void initializeContext(SyntaxTree tree, Environment environment) {
        if (tree == null) return;
        Context context = new Context();
        tree.context(context);
        tree.children().forEach(t -> initializeContext(t, environment));
    }

    /**
     * Detect module information and create symbol table according to grammar structures
     */
    @DefinePass
    public static void initializeScopes(SyntaxTree tree, Environment environment) {
        if (tree == null) return;
        Context context = tree.context();
        switch (tree) {
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
                ModuleHeader header = source.header;
                Module module = environment.foundModule(header.name);
                context.setCurrentModule(module);
                context.setSymbolTable(new SymbolTable());
                SymbolTable symbolTable = new SymbolTable(context.getSymbolTable());
                source.statements.stream().map(SyntaxTree::context).forEach(c -> c.setSymbolTable(symbolTable));
            }
            case TraitDeclaration ignored -> context.setSymbolTable(new SymbolTable(context.getSymbolTable()));
            default -> tree.children().stream().filter(Objects::nonNull).map(SyntaxTree::context).forEach(c -> c.setSymbolTable(context.getSymbolTable()));
        }
        tree.children().forEach(t -> initializeScopes(t, environment));
    }
}
