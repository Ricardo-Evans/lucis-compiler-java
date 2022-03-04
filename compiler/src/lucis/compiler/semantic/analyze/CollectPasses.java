package lucis.compiler.semantic.analyze;

import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.ClassType;
import lucis.compiler.semantic.concept.LucisFunction;
import lucis.compiler.semantic.concept.LucisKind;
import lucis.compiler.semantic.concept.TraitType;
import lucis.compiler.syntax.*;

public final class CollectPasses {
    private CollectPasses() {
    }

    public static void collectSymbol(SyntaxTree tree, Environment environment) {
        if (tree == null) return;
        Context context = tree.context();
        switch (tree) {
            case ClassDeclaration declaration -> {
                LucisKind typeKind = Utility.lucisType(environment);
                Module module = context.getCurrentModule();
                ClassType type = new ClassType(module.name(), declaration.name);
                context.setBuildingObject(type);
                Symbol symbol = new Symbol(type.name(), typeKind.apply(type), false, type);
                module.foundSymbol(type.name(), symbol);
            }
            case FunctionDeclaration declaration -> {
                Module module = context.getCurrentModule();
                LucisFunction function = new LucisFunction();
                context.setBuildingObject(function);
            }
            case Source source -> {
                if (Utility.LUCIS_CORE.equals(source.header.name)) break;
                Module coreModule = environment.requireCoreModule();
                SymbolTable symbolTable = context.getSymbolTable();
                coreModule.symbols().forEach((name, symbols) -> {
                    symbolTable.foundMultipleSymbols(name, symbols);
                    symbolTable.foundMultipleSymbols(coreModule.name() + ":" + name, symbols);
                });
            }
            case TraitDeclaration declaration -> {
                LucisKind typeKind = Utility.lucisType(environment);
                Module module = context.getCurrentModule();
                TraitType type = new TraitType(module.name(), declaration.name);
                context.setBuildingObject(type);
                Symbol symbol = new Symbol(type.name(), typeKind.apply(type), false, type);
                module.foundSymbol(type.name(), symbol);
            }
            case default -> {
            }
        }
        tree.children().forEach(t -> collectSymbol(t, environment));
    }
}
