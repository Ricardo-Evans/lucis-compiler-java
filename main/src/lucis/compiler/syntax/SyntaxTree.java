package lucis.compiler.syntax;

import compiler.entity.Position;
import lucis.compiler.semantic.Context;

import java.util.List;

public abstract class SyntaxTree implements compiler.entity.SyntaxTree<SyntaxTree> {
    private Position position;
    protected Context context = new Context();
    protected List<SyntaxTree> children;

    protected SyntaxTree(SyntaxTree... children) {
        this.children = List.of(children);
        for (SyntaxTree tree : children) tree.context.parent(context);
    }

    public SyntaxTree position(Position position) {
        this.position = position;
        return this;
    }

    @Override
    public Position position() {
        return position;
    }

    public Context context() {
        return context;
    }

    @Override
    public List<SyntaxTree> children() {
        return children;
    }

    public abstract void visit(Visitor visitor);

    public interface Visitor {
        void visitAssignStatement(AssignStatement statement);

        void visitBlockStatement(BlockStatement statement);

        void visitBranchStatement(BranchStatement statement);

        void visitClassStatement(ClassStatement statement);

        void visitDefineStatement(DefineStatement statement);

        void visitDiscardStatement(DiscardStatement statement);

        void visitDoubleOperatorExpression(DoubleOperatorExpression expression);

        void visitExportStatement(ExportStatement statement);

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
