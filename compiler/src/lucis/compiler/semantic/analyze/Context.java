package lucis.compiler.semantic.analyze;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.concept.LucisObject;
import lucis.compiler.semantic.concept.LucisType;

import java.util.Objects;
import java.util.Optional;

public class Context {
    private Module currentModule;
    private LucisObject buildingObject;
    private SymbolTable symbolTable;

    public Module getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(Module module) {
        Objects.requireNonNull(module);
        if (this.currentModule != null)
            throw new SemanticException("cannot define module " + module + " in module " + this.currentModule.name());
        this.currentModule = module;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public LucisObject getBuildingObject() {
        return buildingObject;
    }

    public void setBuildingObject(LucisObject buildingObject) {
        this.buildingObject = buildingObject;
    }
}
