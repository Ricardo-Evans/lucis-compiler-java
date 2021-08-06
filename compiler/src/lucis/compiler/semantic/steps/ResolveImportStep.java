package lucis.compiler.semantic.steps;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.semantic.Context;
import lucis.compiler.semantic.Environment;
import lucis.compiler.semantic.LucisModule;
import lucis.compiler.syntax.ModuleHeader;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;

import java.util.Objects;

public class ResolveImportStep implements Step<SyntaxTree, Environment> {
    @Override
    public boolean process(SyntaxTree tree, Environment environment) {
        Context context = tree.context();
        if (tree instanceof Source source) {
            ModuleHeader header = source.header;
            header.imports.forEach(i -> {
                LucisModule module = environment.findModule(i.module()).orElseThrow(() -> new SemanticException("import module " + i.module() + " not found"));
                if (Objects.equals("_", i.name())) module.symbols().forEach(context::importSymbols);
                else context.importSymbols(i.name(), module.findSymbol(i.name()));
                context.importCurrentModule();
            });
        }
        return false;
    }
}
