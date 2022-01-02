package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.analyze.Context;
import lucis.compiler.semantic.analyze.Symbol;
import lucis.compiler.semantic.concept.Element;
import lucis.compiler.semantic.concept.LucisKind;
import lucis.compiler.semantic.concept.LucisType;

public final class Utility {
    public static final String LUCIS_CORE = "lucis.core";
    public static final String LUCIS_TYPE = LUCIS_CORE + ":Type";
    public static final String LUCIS_STRING = LUCIS_CORE + ":String";
    public static final String LUCIS_INTEGER = LUCIS_CORE + ":Integer";
    public static final String LUCIS_DECIMAL = LUCIS_CORE + ":Decimal";

    private Utility() {
    }

    public static LucisKind TypeKind(Context context) {
        return (LucisKind) context.getSymbolTable().findSymbol(LUCIS_TYPE).map(Symbol::unique).flatMap(Element::getValue).orElseThrow(() -> new SemanticException(""));
    }

    public static LucisType StringType(Context context) {
        return (LucisType) context.getSymbolTable().findSymbol(LUCIS_STRING).map(Symbol::unique).flatMap(Element::getValue).orElseThrow(() -> new SemanticException(""));
    }
}
