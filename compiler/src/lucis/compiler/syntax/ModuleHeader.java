package lucis.compiler.syntax;

import java.util.LinkedList;
import java.util.List;

public class ModuleHeader {
    public final UniqueIdentifier name;
    public final List<UniqueIdentifier> imports = new LinkedList<>();
    public final List<UniqueIdentifier> exports = new LinkedList<>();

    public ModuleHeader(UniqueIdentifier name) {
        super();
        this.name = name;
    }
}
