package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.semantic.concept.ClassType;
import lucis.compiler.semantic.concept.Constant;
import lucis.compiler.semantic.concept.LucisType;
import lucis.compiler.semantic.concept.TraitType;
import lucis.compiler.syntax.*;

public class CollectTypeStep implements Step<SyntaxTree, Environment> {
    @Override
    public void process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        switch (tree) {
            case ClassDeclaration declaration -> {
                Module module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + declaration.name + " is defined outside any module"));
                LucisType type = new ClassType(module.name(), declaration.name);
                module.findSymbol(declaration.name).foundElement(new Constant(type, declaration.name, type));
            }
            case TraitDeclaration declaration -> {
                Module module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + declaration.name + " is defined outside any module"));
                LucisType type = new TraitType(module.name(), declaration.name);
                module.findSymbol(declaration.name).foundElement(new Constant(type, declaration.name, type));
            }
            default -> {
            }
        }
    }
}
