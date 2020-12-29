package lucis.compiler.syntax;

import lucis.compiler.entity.Position;

public abstract class SyntaxTree {
    private Position position;

    protected SyntaxTree() {
    }

    public Position position() {
        return position;
    }

    public SyntaxTree position(Position position) {
        this.position = position;
        return this;
    }

    public abstract <T> T visit(Visitor<T> visitor, T data);

    public interface Visitor<T> {
        T visitAssignStatement(AssignStatement statement, T data);

        T visitBlockExpression(BlockExpression expression, T data);

        T visitBranchStatement(BranchStatement statement, T data);

        T visitClassStatement(ClassStatement statement, T data);

        T visitDefineStatement(DefineStatement statement, T data);

        T visitDiscardStatement(DiscardStatement statement, T data);

        T visitDoubleOperatorExpression(DoubleOperatorExpression expression, T data);

        T visitExportStatement(ExportStatement statement, T data);

        T visitExpressionStatement(ExpressionStatement statement, T data);

        T visitFunctionExpression(FunctionExpression expression, T data);

        T visitFunctionStatement(FunctionStatement statement, T data);

        T visitIdentifierExpression(IdentifierExpression expression, T data);

        T visitImportStatement(ImportStatement statement, T data);

        T visitIndexExpression(IndexExpression expression, T data);

        T visitLiteralExpression(LiteralExpression expression, T data);

        T visitReturnStatement(ReturnStatement statement, T data);

        T visitSingleOperatorExpression(SingleOperatorExpression expression, T data);

        T visitSource(Source source, T data);

        T visitTraitStatement(TraitStatement statement, T data);
    }

    public interface SearchVisitor<T> extends Visitor<T> {
        @Override
        default T visitAssignStatement(AssignStatement statement, T data) {
            data = statement.content.visit(this, data);
            return data;
        }

        @Override
        default T visitBlockExpression(BlockExpression expression, T data) {
            for (SyntaxTree tree : expression.statements) data = tree.visit(this, data);
            return data;
        }

        @Override
        default T visitBranchStatement(BranchStatement statement, T data) {
            data = statement.condition.visit(this, data);
            if (statement.positive != null) data = statement.positive.visit(this, data);
            if (statement.negative != null) data = statement.negative.visit(this, data);
            return data;
        }

        @Override
        default T visitClassStatement(ClassStatement statement, T data) {
            for (SyntaxTree tree : statement.bases) data = tree.visit(this, data);
            return data;
        }

        @Override
        default T visitDefineStatement(DefineStatement statement, T data) {
            data = statement.type.visit(this, data);
            data = statement.value.visit(this, data);
            return data;
        }

        @Override
        default T visitDiscardStatement(DiscardStatement statement, T data) {
            data = statement.expression.visit(this, data);
            return data;
        }

        @Override
        default T visitDoubleOperatorExpression(DoubleOperatorExpression expression, T data) {
            data = expression.expression1.visit(this, data);
            data = expression.expression2.visit(this, data);
            return data;
        }

        @Override
        default T visitExportStatement(ExportStatement statement, T data) {
            data = statement.expression.visit(this, data);
            return data;
        }

        @Override
        default T visitExpressionStatement(ExpressionStatement statement, T data) {
            data = statement.expression.visit(this, data);
            return data;
        }

        @Override
        default T visitFunctionExpression(FunctionExpression expression, T data) {
            data = expression.function.visit(this, data);
            for (SyntaxTree tree : expression.parameters) data = tree.visit(this, data);
            return data;
        }

        @Override
        default T visitFunctionStatement(FunctionStatement statement, T data) {
            data = statement.type.visit(this, data);
            data = statement.body.visit(this, data);
            return data;
        }

        @Override
        default T visitIdentifierExpression(IdentifierExpression expression, T data) {
            data = expression.parent.visit(this, data);
            return data;
        }

        @Override
        default T visitImportStatement(ImportStatement statement, T data) {
            data = statement.expression.visit(this, data);
            return data;
        }

        @Override
        default T visitIndexExpression(IndexExpression expression, T data) {
            data = expression.array.visit(this, data);
            data = expression.index.visit(this, data);
            return data;
        }

        @Override
        default T visitLiteralExpression(LiteralExpression expression, T data) {
            return data;
        }

        @Override
        default T visitReturnStatement(ReturnStatement statement, T data) {
            data = statement.value.visit(this, data);
            return data;
        }

        @Override
        default T visitSingleOperatorExpression(SingleOperatorExpression expression, T data) {
            data = expression.expression.visit(this, data);
            return data;
        }

        @Override
        default T visitSource(Source source, T data) {
            for (SyntaxTree tree : source.statements) data = tree.visit(this, data);
            return data;
        }

        @Override
        default T visitTraitStatement(TraitStatement statement, T data) {
            for (SyntaxTree tree : statement.bases) data = tree.visit(this, data);
            for (SyntaxTree tree : statement.statements) data = tree.visit(this, data);
            return data;
        }
    }

    public interface IgnoreVisitor<T> extends Visitor<T> {
        @Override
        default T visitAssignStatement(AssignStatement statement, T data) {
            return data;
        }

        @Override
        default T visitBlockExpression(BlockExpression expression, T data) {
            return data;
        }

        @Override
        default T visitBranchStatement(BranchStatement statement, T data) {
            return data;
        }

        @Override
        default T visitClassStatement(ClassStatement statement, T data) {
            return data;
        }

        @Override
        default T visitDefineStatement(DefineStatement statement, T data) {
            return data;
        }

        @Override
        default T visitDiscardStatement(DiscardStatement statement, T data) {
            return data;
        }

        @Override
        default T visitDoubleOperatorExpression(DoubleOperatorExpression expression, T data) {
            return data;
        }

        @Override
        default T visitExportStatement(ExportStatement statement, T data) {
            return data;
        }

        @Override
        default T visitExpressionStatement(ExpressionStatement statement, T data) {
            return data;
        }

        @Override
        default T visitFunctionExpression(FunctionExpression expression, T data) {
            return data;
        }

        @Override
        default T visitFunctionStatement(FunctionStatement statement, T data) {
            return data;
        }

        @Override
        default T visitIdentifierExpression(IdentifierExpression expression, T data) {
            return data;
        }

        @Override
        default T visitImportStatement(ImportStatement statement, T data) {
            return data;
        }

        @Override
        default T visitIndexExpression(IndexExpression expression, T data) {
            return data;
        }

        @Override
        default T visitLiteralExpression(LiteralExpression expression, T data) {
            return data;
        }

        @Override
        default T visitReturnStatement(ReturnStatement statement, T data) {
            return data;
        }

        @Override
        default T visitSingleOperatorExpression(SingleOperatorExpression expression, T data) {
            return data;
        }

        @Override
        default T visitSource(Source source, T data) {
            return data;
        }

        @Override
        default T visitTraitStatement(TraitStatement statement, T data) {
            return data;
        }
    }
}
