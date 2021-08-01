package lucis.compiler.syntax;

import java.util.LinkedList;
import java.util.List;

public class ModuleHeader {
    public final NestedIdentifier name;
    public final List<NestedIdentifier> imports = new LinkedList<>();
    public final List<NestedIdentifier> exports = new LinkedList<>();

    public ModuleHeader(NestedIdentifier name) {
        super();
        this.name = name;
    }
}
