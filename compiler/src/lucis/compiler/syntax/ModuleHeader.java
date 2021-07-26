package lucis.compiler.syntax;

import java.util.LinkedList;
import java.util.List;

public class ModuleHeader {
    public final Symbol name;
    public final List<Symbol> imports = new LinkedList<>();
    public final List<Symbol> exports = new LinkedList<>();

    public ModuleHeader(Symbol name) {
        super();
        this.name = name;
    }
}
