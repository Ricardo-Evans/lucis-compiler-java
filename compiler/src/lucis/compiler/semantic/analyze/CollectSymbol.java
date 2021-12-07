package lucis.compiler.semantic.analyze;

import lucis.compiler.semantic.AnalyzeStep;
import lucis.compiler.semantic.concept.ClassType;
import lucis.compiler.semantic.concept.Constant;
import lucis.compiler.semantic.concept.LucisFunction;
import lucis.compiler.semantic.concept.TraitType;
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
                Module module = context.getCurrentModule();
                ClassType type = new ClassType(module.name(), declaration.name);
                context.setBuildingObject(type);
                module.foundSymbol(type.name());
                module.findSymbol(type.name()).foundElement(new Constant(type, type.name(), type));
            }
            case FunctionDeclaration declaration -> {
                Module module = context.getCurrentModule();
                LucisFunction function = new LucisFunction();
                context.setBuildingObject(function);
            }
            case TraitDeclaration declaration -> {
                Module module = context.getCurrentModule();
                TraitType type = new TraitType(module.name(), declaration.name);
                context.setBuildingObject(type);
                module.foundSymbol(type.name());
                module.findSymbol(type.name()).foundElement(new Constant(type, type.name(), type));
            }
        }
    }
}
