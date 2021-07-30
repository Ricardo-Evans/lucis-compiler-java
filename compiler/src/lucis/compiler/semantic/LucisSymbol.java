package lucis.compiler.semantic;

public record LucisSymbol(String name, String fullName, Kind kind) {
    public enum Kind {
        FUNCTION,
        VARIABLE,
    }
}
