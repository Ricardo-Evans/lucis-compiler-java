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
            case ClassStatement statement -> {
                LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + statement.name + " is defined outside any module"));
                LucisType type = new ClassType(module.name(), statement.name);
                module.findSymbol(statement.name).foundElement(new LucisConstant(type, statement.name, type));
            }
            case TraitStatement statement -> {
                LucisModule module = context.getCurrentModule().orElseThrow(() -> new SemanticException("type " + statement.name + " is defined outside any module"));
                LucisType type = new TraitType(module.name(), statement.name);
                module.findSymbol(statement.name).foundElement(new LucisConstant(type, statement.name, type));
            }
            default -> {
            }
        }
    }
}
