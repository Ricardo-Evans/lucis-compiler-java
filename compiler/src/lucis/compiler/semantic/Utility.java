package lucis.compiler.semantic;

import compiler.semantic.SemanticException;

import java.util.List;
import java.util.StringJoiner;

public final class Utility {
    private Utility() {
    }

    public static String calculateSignature(LucisType result, LucisType... parameters) {
        StringJoiner joiner = new StringJoiner(",");
        for (LucisType type : parameters) {
            joiner.add(type.fullName());
        }
        return result.fullName() + '(' + joiner + ')';
    }

    public static LucisType uniqueType(String name, Context context, Environment environment) {
        List<LucisSymbol> symbols = context.findSymbol(name).stream().filter(s -> s.kind() == LucisSymbol.Kind.TYPE).toList();
        if (symbols.size() > 1) throw new SemanticException(name + " is ambitious");
        if (symbols.size() <= 0) throw new SemanticException(name + " not found");
        LucisSymbol symbol = symbols.get(0);
        return environment.findModule(symbol.module()).orElseThrow(() -> new SemanticException("module " + symbol.module() + " not found"))
                .findType(symbol).orElseThrow(() -> new SemanticException(symbol + " not found"));
    }
}
