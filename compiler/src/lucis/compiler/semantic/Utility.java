package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.analyze.Environment;
import lucis.compiler.semantic.analyze.Symbol;
import lucis.compiler.semantic.concept.LucisKind;
import lucis.compiler.semantic.concept.LucisType;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public final class Utility {
    public static final String LUCIS_CORE = "lucis.core";
    public static final String LUCIS_DELIMITER_MODULE_SYMBOL = ":";
    public static final String LUCIS_SIGNATURE_DELIMITER = "#";

    public static final String LUCIS_TYPE = signature(LUCIS_CORE, "Type");
    public static final String LUCIS_KIND = signature(LUCIS_CORE, "Kind");
    public static final String LUCIS_STRING = signature(LUCIS_CORE, "String");
    public static final String LUCIS_INTEGER = signature(LUCIS_CORE, "Integer");
    public static final String LUCIS_DECIMAL = signature(LUCIS_CORE, "Decimal");

    private Utility() {
    }

    public static LucisKind lucisType(Environment environment) {
        return (LucisKind) environment.requireCoreModule()
                .findSymbol(LUCIS_TYPE, null) // TODO
                .value();
    }

    public static <T> BinaryOperator<T> unique(Supplier<? extends RuntimeException> supplier) {
        return (t1, t2) -> {
            throw supplier.get();
        };
    }

    public static Supplier<SemanticException> symbolNotFound(String name, LucisType type) {
        return () -> new SemanticException("symbol of name: " + name + " with type: " + type + " not found");
    }

    public static Supplier<SemanticException> symbolNotFound(String signature) {
        return () -> new SemanticException("symbol of signature: " + signature + " not found");
    }

    public static Supplier<SemanticException> moduleNotFound(String name) {
        return () -> new SemanticException("module of name: " + name + " not found");
    }

    public static Supplier<SemanticException> recursiveModuleLoading(String module) {
        return () -> new SemanticException("trying to load module of name: " + module + " recursively");
    }

    public static Supplier<SemanticException> ambitiousSymbolsFound(String name, LucisType type) {
        return () -> new SemanticException("symbol of name: " + name + " with type: " + type + " is ambitious");
    }

    public static Supplier<SemanticException> symbolAlreadyExist(Symbol symbol) {
        return () -> new SemanticException("symbol: " + symbol + " already exists");
    }

    public static String signature(String module, String name) {
        return module + LUCIS_DELIMITER_MODULE_SYMBOL + name;
    }
}
