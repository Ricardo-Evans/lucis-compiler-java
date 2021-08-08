package lucis.compiler.syntax;

import compiler.entity.Position;
import lucis.compiler.semantic.Context;

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

    public abstract <T> T visit(Visitor<T> visitor);

    public interface Visitor<T> {
        T visitAssignStatement(AssignStatement statement);

        T visitBinaryOperatorExpression(BinaryOperatorExpression expression);

        T visitBlockStatement(BlockStatement statement);

        T visitBranchStatement(BranchStatement statement);

        T visitClassStatement(ClassStatement statement);

        T visitDefineStatement(DefineStatement statement);

        T visitDiscardStatement(DiscardStatement statement);

        T visitFunctionExpression(FunctionExpression expression);

        T visitFunctionStatement(FunctionStatement statement);

        T visitIdentifierExpression(ElementExpression expression);

        T visitIndexExpression(IndexExpression expression);

        T visitLiteralExpression(LiteralExpression expression);

        T visitReturnStatement(ReturnStatement statement);

        T visitSource(Source source);

        T visitTraitStatement(TraitStatement statement);

        T visitUnaryOperatorExpression(UnaryOperatorExpression expression);
    }
}
