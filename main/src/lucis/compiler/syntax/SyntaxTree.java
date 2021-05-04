package lucis.compiler.syntax;

import compiler.entity.Position;
import lucis.compiler.semantic.Context;

public abstract class SyntaxTree implements compiler.entity.SyntaxTree<Context, SyntaxTree.Visitor> {
    private Position position;
    private Context context;
    private SyntaxTree[] children;

    protected SyntaxTree() {
    }

    public SyntaxTree position(Position position) {
        this.position = position;
        return this;
    }

    @Override
    public Position position() {
        return position;
    }

    @Override
    public Context context() {
        return context;
    }

    @Override
    public SyntaxTree[] children() {
        return children;
    }

    @Override
    public abstract void visit(Visitor visitor);

    public interface Visitor {
        void visitAssignStatement(AssignStatement statement);

        void visitBlockExpression(BlockExpression expression);

        void visitBranchStatement(BranchStatement statement);

        void visitClassStatement(ClassStatement statement);

        void visitDefineStatement(DefineStatement statement);

        void visitDiscardStatement(DiscardStatement statement);

        void visitDoubleOperatorExpression(DoubleOperatorExpression expression);

        void visitExportStatement(ExportStatement statement);

        void visitExpressionStatement(ExpressionStatement statement);

        void visitFunctionExpression(FunctionExpression expression);

        void visitFunctionStatement(FunctionStatement statement);

        void visitIdentifierExpression(ElementExpression expression);

        void visitImportStatement(ImportStatement statement);

        void visitIndexExpression(IndexExpression expression);

        void visitLiteralExpression(LiteralExpression expression);

        void visitReturnStatement(ReturnStatement statement);

        void visitSingleOperatorExpression(SingleOperatorExpression expression);

        void visitSource(Source source);

        void visitTraitStatement(TraitStatement statement);
    }
}
