package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.AnalyzeStep;
import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.*;
import lucis.compiler.syntax.ClassDeclaration;
import lucis.compiler.syntax.FunctionDeclaration;
import lucis.compiler.syntax.SyntaxTree;
import lucis.compiler.syntax.TraitDeclaration;

public class CollectSymbol implements AnalyzeStep {
    @Override
    public void process(SyntaxTree tree, Context context, Environment environment) {
        switch (tree) {
            case null, default -> {
            }
            case ClassDeclaration declaration -> {
                LucisKind typeKind = Utility.TypeKind(context);
                Module module = context.getCurrentModule();
                ClassType type = new ClassType(module.name(), declaration.name);
                context.setBuildingObject(type);
                module.foundSymbol(type.name());
                module.findSymbol(type.name()).foundElement(new Element(type.name(), typeKind.apply(type), false, type));
            }
            case FunctionDeclaration declaration -> {
                Module module = context.getCurrentModule();
                LucisFunction function = new LucisFunction();
                context.setBuildingObject(function);
            }
            case TraitDeclaration declaration -> {
                LucisKind typeKind = Utility.TypeKind(context);
                Module module = context.getCurrentModule();
                TraitType type = new TraitType(module.name(), declaration.name);
                context.setBuildingObject(type);
                module.foundSymbol(type.name());
                module.findSymbol(type.name()).foundElement(new Element(type.name(), typeKind.apply(type), false, type));
            }
        }
    }
}
