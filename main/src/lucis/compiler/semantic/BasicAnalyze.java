package lucis.compiler.semantic;

import compiler.semantic.SemanticException;
import lucis.compiler.ir.LucisType;
import lucis.compiler.ir.LucisVariable;
import lucis.compiler.syntax.*;

public class BasicAnalyze implements SyntaxTree.Visitor {
    @Override
    public void visitAssignStatement(AssignStatement statement) {
    }

    @Override
    public void visitBlockStatement(BlockStatement statement) {
    }

    @Override
    public void visitBranchStatement(BranchStatement statement) {

    }

    @Override
    public void visitClassStatement(ClassStatement statement) {
        Context context = statement.context();
        String typename = statement.name;
        LucisType type = new LucisType(typename, context.findModule().orElseThrow(() -> new SemanticException("cannot define type " + typename + " in no module")));
        context.foundType(typename, type);
    }

    @Override
    public void visitDefineStatement(DefineStatement statement) {
        Context context = statement.context();
        String typename = statement.type;
        String identifier = statement.identifier;
        LucisVariable variable = new LucisVariable(typename, identifier);
        context.foundVariable(identifier, variable);
    }

    @Override
    public void visitDiscardStatement(DiscardStatement statement) {

    }

    @Override
    public void visitDoubleOperatorExpression(DoubleOperatorExpression expression) {

    }

    @Override
    public void visitExportStatement(ExportStatement statement) {

    }

    @Override
    public void visitFunctionExpression(FunctionExpression expression) {

    }

    @Override
    public void visitFunctionStatement(FunctionStatement statement) {
        Context context = statement.context();
        String identifier = statement.identifier;
    }

    @Override
    public void visitIdentifierExpression(ElementExpression expression) {

    }

    @Override
    public void visitImportStatement(ImportStatement statement) {
        statement.context().importModule(statement.identifier);
    }

    @Override
    public void visitIndexExpression(IndexExpression expression) {

    }

    @Override
    public void visitLiteralExpression(LiteralExpression expression) {

    }

    @Override
    public void visitReturnStatement(ReturnStatement statement) {

    }

    @Override
    public void visitSingleOperatorExpression(SingleOperatorExpression expression) {

    }

    @Override
    public void visitSource(Source source) {

    }

    @Override
    public void visitTraitStatement(TraitStatement statement) {
        Context context = statement.context();
        String typename = statement.name;
        LucisType type = new LucisType(typename, context.findModule().orElseThrow(() -> new SemanticException("cannot define type " + typename + " in no module")));
        context.foundType(typename, type);
    }
}
