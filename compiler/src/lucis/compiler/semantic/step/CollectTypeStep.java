package lucis.compiler.semantic.step;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.semantic.*;
import lucis.compiler.semantic.type.ClassType;
import lucis.compiler.semantic.LucisType;
import lucis.compiler.semantic.type.TraitType;
import lucis.compiler.syntax.*;

public class CollectTypeStep implements Step<SyntaxTree, Environment> {
    @Override
    public void process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        switch (tree) {
            case ClassDeclaration declaration -> {
                LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + declaration.name + " is defined outside any module"));
                LucisType type = new ClassType(module.name(), declaration.name);
                module.findSymbol(declaration.name).foundElement(new LucisConstant(type, declaration.name, type));
            }
            case TraitDeclaration declaration -> {
                LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + declaration.name + " is defined outside any module"));
                LucisType type = new TraitType(module.name(), declaration.name);
                module.findSymbol(declaration.name).foundElement(new LucisConstant(type, declaration.name, type));
            }
            default -> {
            }
        }
    }
}
