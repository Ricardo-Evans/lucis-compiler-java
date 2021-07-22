package lucis.compiler.syntax;

public class ModuleStatement extends Statement {
    public final String name;

    public ModuleStatement(String name) {
        this.name = name;
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitModuleStatement(this);
    }
}
