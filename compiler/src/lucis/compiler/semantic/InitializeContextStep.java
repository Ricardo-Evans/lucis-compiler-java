package lucis.compiler.semantic;

import compiler.semantic.Step;
import lucis.compiler.syntax.*;

public class InitializeContextStep implements Step<SyntaxTree, Environment>, SyntaxTree.Visitor<Boolean> {
    @Override
    public boolean process(SyntaxTree tree, Environment environment) {
        Boolean result = tree.visit(this);
        if (result != null) return result;
        return true;
    }

    @Override
    public Boolean visitAssignStatement(AssignStatement statement) {
        Context context = statement.context();
        statement.right.context(context);
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
        return false;
    }

    @Override
    public Boolean visitDefineStatement(DefineStatement statement) {
        statement.value.context(statement.context());
        return true;
    }

    @Override
    public Boolean visitDiscardStatement(DiscardStatement statement) {
        statement.expression.context(statement.context());
        return true;
    }

    @Override
    public Boolean visitDoubleOperatorExpression(DoubleOperatorExpression expression) {
        Context context = expression.context();
        expression.expression1.context(context);
        expression.expression2.context(context);
        return true;
    }

    @Override
    public Boolean visitFunctionExpression(FunctionExpression expression) {
        Context context = expression.context();
        expression.function.context(context);
        expression.parameters.forEach(e -> e.context(context));
        return true;
    }

    @Override
    public Boolean visitFunctionStatement(FunctionStatement statement) {
        Context context = new Context(statement.context());
        statement.body.context(context);
        return true;
    }

    @Override
    public Boolean visitIdentifierExpression(ElementExpression expression) {
        if (expression.parent != null) expression.parent.context(expression.context());
        return true;
    }

    @Override
    public Boolean visitIndexExpression(IndexExpression expression) {
        Context context = expression.context();
        expression.array.context(context);
        expression.index.context(context);
        return true;
    }

    @Override
    public Boolean visitLiteralExpression(LiteralExpression expression) {
        return false;
    }

    @Override
    public Boolean visitReturnStatement(ReturnStatement statement) {
        statement.value.context(statement.context());
        return true;
    }

    @Override
    public Boolean visitSingleOperatorExpression(SingleOperatorExpression expression) {
        expression.expression.context(expression.context());
        return true;
    }

    @Override
    public Boolean visitSource(Source source) {
        Context context = new Context(null);
        source.context(context);
        source.statements.forEach(s -> s.context(context));
        return true;
    }

    @Override
    public Boolean visitTraitStatement(TraitStatement statement) {
        return true;
    }
}
