package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import lucis.compiler.syntax.UniqueIdentifier;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

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

    public static LucisType uniqueType(UniqueIdentifier identifier, Context context, Environment environment) {
        List<LucisSymbol> symbols = context.findSymbol(identifier.name()).stream()
                .filter(s -> s.kind() == LucisSymbol.Kind.TYPE)
                .filter(s -> identifier.module() == null || Objects.equals(s.module(), identifier.module()))
                .toList();
        if (symbols.size() > 1) throw new SemanticException(identifier + " is ambitious");
        if (symbols.size() <= 0) throw new SemanticException(identifier + " not found");
        LucisSymbol symbol = symbols.get(0);
        return environment.findModule(symbol.module()).orElseThrow(() -> new SemanticException("module " + symbol.module() + " not found"))
                .findType(symbol).orElseThrow(() -> new SemanticException(symbol + " not found"));
    }
}
