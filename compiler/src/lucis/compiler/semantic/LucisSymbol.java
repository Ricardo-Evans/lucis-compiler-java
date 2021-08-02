package lucis.compiler.semantic;

public record LucisSymbol(String name, String fullName, String signature, Kind kind) {
    public LucisSymbol(String name, String fullName, Kind kind) {
        this(name, fullName, null, kind);
    }

    public enum Kind {
        TYPE,
        FUNCTION,
        VARIABLE,
    }
}
