package lucis.compiler.syntax;

import compiler.entity.Position;
import lucis.compiler.semantic.analyze.Context;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SyntaxTree implements compiler.entity.SyntaxTree<SyntaxTree> {
    private Position position;
    private Context context;
    protected List<? extends SyntaxTree> children;

    protected SyntaxTree(SyntaxTree... children) {
        this(Arrays.stream(children).filter(Objects::nonNull).toList());
    }

    protected SyntaxTree(Stream<? extends SyntaxTree> children) {
        this(children.collect(Collectors.toList()));
    }

    protected SyntaxTree(List<? extends SyntaxTree> children) {
        this.children = children;
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

    public void context(Context context) {
        this.context = context;
    }

    @Override
    public List<? extends SyntaxTree> children() {
        return children;
    }

    public abstract void visit(Visitor visitor);

    public interface Visitor {
        void visitAssignStatement(AssignStatement statement);

        void visitBinaryOperatorExpression(BinaryOperatorExpression expression);

        void visitBlockStatement(BlockStatement statement);

        void visitBranchStatement(BranchStatement statement);

        void visitClassStatement(ClassDeclaration statement);

        void visitDefineStatement(DefineStatement statement);

        void visitDiscardStatement(DiscardStatement statement);

        void visitFunctionExpression(FunctionExpression expression);

        void visitFunctionStatement(FunctionDeclaration statement);

        void visitIdentifierExpression(ElementExpression expression);

        void visitIndexExpression(IndexExpression expression);

        void visitLiteralExpression(LiteralExpression expression);

        void visitReturnStatement(ReturnStatement statement);

        void visitSource(Source source);

        void visitTraitStatement(TraitDeclaration statement);

        void visitUnaryOperatorExpression(UnaryOperatorExpression expression);
    }
}
