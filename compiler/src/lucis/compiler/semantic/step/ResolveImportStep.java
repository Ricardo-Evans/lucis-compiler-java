package lucis.compiler.semantic.step;

import compiler.semantic.SemanticException;
import compiler.semantic.Step;
import lucis.compiler.semantic.Context;
import lucis.compiler.semantic.Environment;
import lucis.compiler.semantic.Utility;
import lucis.compiler.syntax.ModuleHeader;
import lucis.compiler.syntax.Source;
import lucis.compiler.syntax.SyntaxTree;

import java.util.Objects;

public class ResolveImportStep implements Step<SyntaxTree, Environment> {
    @Override
    public void process(SyntaxTree tree, Environment environment) {
        if (tree instanceof Source source) {
            ModuleHeader header = source.header;
            Context context = tree.context();
            if (!Objects.equals(context.requireCurrentModule().name(), Utility.LUCIS_CORE))
                context.importModule(environment.findModule(Utility.LUCIS_CORE).orElseThrow(() -> new SemanticException("no lucis core module detected")));
            context.importCurrentModule();
            header.imports.forEach(i -> context.importModule(environment.requireModule(i.module())));
        }
    }
}
