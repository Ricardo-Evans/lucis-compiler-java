package lucis.compiler.syntax;

import java.util.LinkedList;
import java.util.List;

public class ModuleHeader {
    public final String name;
    public final List<UniqueIdentifier> imports = new LinkedList<>();
    public final List<UniqueIdentifier> exports = new LinkedList<>();

    public ModuleHeader(String name) {
        this.name = name;
    }
}
