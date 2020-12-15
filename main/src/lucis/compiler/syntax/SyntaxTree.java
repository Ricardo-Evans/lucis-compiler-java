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

    public abstract <R, D> R visit(Visitor<R, D> visitor, D data);

    public interface Visitor<R, D> {
        R visitAssignStatement(AssignStatement statement, D data);

        R visitBlockExpression(BlockExpression expression, D data);

        R visitBranchStatement(BranchStatement statement, D data);

        R visitClassStatement(ClassStatement statement, D data);

        R visitDefineStatement(DefineStatement statement, D data);

        R visitDiscardStatement(DiscardStatement statement, D data);

        R visitDoubleOperatorExpression(DoubleOperatorExpression expression, D data);

        R visitExportStatement(ExportStatement statement, D data);

        R visitExpressionStatement(ExpressionStatement statement, D data);

        R visitFunctionExpression(FunctionExpression expression, D data);

        R visitFunctionStatement(FunctionStatement statement, D data);

        R visitIdentifierExpression(IdentifierExpression expression, D data);

        R visitImportStatement(ImportStatement statement, D data);

        R visitIndexExpression(IndexExpression expression, D data);

        R visitLiteralExpression(LiteralExpression expression, D data);

        R visitReturnStatement(ReturnStatement statement, D data);

        R visitSingleOperatorExpression(SingleOperatorExpression expression, D data);

        R visitSource(Source source, D data);

        R visitTraitStatement(TraitStatement statement, D data);
    }
}
