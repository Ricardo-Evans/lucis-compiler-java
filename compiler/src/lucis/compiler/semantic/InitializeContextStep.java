package lucis.compiler.semantic;

import compiler.semantic.Step;
import lucis.compiler.syntax.*;

public class InitializeContextStep implements Step<SyntaxTree>, SyntaxTree.Visitor<Boolean> {
    @Override
    public boolean process(SyntaxTree tree) {
        Boolean result = tree.visit(this);
        if (result != null) return result;
        return true;
    }

    @Override
    public Boolean visitAssignStatement(AssignStatement statement) {
        Context context = statement.context();
        statement.content.context(context);
        return true;
    }

    @Override
    public Boolean visitBlockStatement(BlockStatement statement) {
        Context context = new Context(statement.context());
        statement.statements.forEach(s -> s.context(context));
        return true;
    }

    @Override
    public Boolean visitBranchStatement(BranchStatement statement) {
        Context positiveContext = new Context(statement.context());
        Context negativeContext = new Context(statement.context());
        statement.condition.context(statement.context());
        statement.positive.context(positiveContext);
        statement.negative.context(negativeContext);
        return true;
    }

    @Override
    public Boolean visitClassStatement(ClassStatement statement) {
        return true;
    }

    @Override
    public Boolean visitDefineStatement(DefineStatement statement) {
        statement.value.context(statement.context());
        return true;
    }

    @Override
    public Boolean visitDiscardStatement(DiscardStatement statement) {
        return null;
    }

    @Override
    public Boolean visitDoubleOperatorExpression(DoubleOperatorExpression expression) {
        return null;
    }

    @Override
    public Boolean visitExportStatement(ExportStatement statement) {
        return null;
    }

    @Override
    public Boolean visitFunctionExpression(FunctionExpression expression) {
        return null;
    }

    @Override
    public Boolean visitFunctionStatement(FunctionStatement statement) {
        return null;
    }

    @Override
    public Boolean visitIdentifierExpression(ElementExpression expression) {
        return null;
    }

    @Override
    public Boolean visitImportStatement(ImportStatement statement) {
        return null;
    }

    @Override
    public Boolean visitIndexExpression(IndexExpression expression) {
        return null;
    }

    @Override
    public Boolean visitLiteralExpression(LiteralExpression expression) {
        return null;
    }

    @Override
    public Boolean visitReturnStatement(ReturnStatement statement) {
        return null;
    }

    @Override
    public Boolean visitSingleOperatorExpression(SingleOperatorExpression expression) {
        return null;
    }

    @Override
    public Boolean visitSource(Source source) {
        Context context = new Context();
        source.context(context);
        source.statements.forEach(s -> s.context(context));
        return true;
    }

    @Override
    public Boolean visitTraitStatement(TraitStatement statement) {
        return null;
    }
}
