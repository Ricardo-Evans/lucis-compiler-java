package lucis.compiler.syntax;

public class UniqueIdentifier {
    public final String name;
    public final UniqueIdentifier child;

    public UniqueIdentifier(String name, UniqueIdentifier child) {
        this.child = child;
        this.name = name;
    }

    public UniqueIdentifier(String name) {
        this(name, null);
    }
}
