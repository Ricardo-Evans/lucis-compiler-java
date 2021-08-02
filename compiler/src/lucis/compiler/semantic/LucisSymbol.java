package lucis.compiler.semantic;

public record LucisSymbol(String name, String module, String signature, Kind kind) {
    public LucisSymbol(String name, String module, Kind kind) {
        this(name, module, null, kind);
    }

    public enum Kind {
        TYPE,
        FUNCTION,
        VARIABLE,
    }
}
