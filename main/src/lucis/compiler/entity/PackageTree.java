package lucis.compiler.entity;

import java.util.HashMap;
import java.util.Map;

public class PackageTree implements SyntaxTree {
    private String name;
    private Map<String, PackageTree> packages = new HashMap<>();
    private Map<String, TypeTree> types = new HashMap<>();
    private Map<String, FunctionTree> functions = new HashMap<>();

    public PackageTree(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, PackageTree> getPackages() {
        return packages;
    }

    public Map<String, TypeTree> getTypes() {
        return types;
    }

    public Map<String, FunctionTree> getFunctions() {
        return functions;
    }

    @Override
    public <R, D> R visit(Visitor<R, D> visitor, D data) {
        return visitor.visitPackage(this, data);
    }
}
