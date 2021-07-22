package lucis.compiler.syntax;

import compiler.entity.Position;
import lucis.compiler.semantic.Context;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class SyntaxTree implements compiler.entity.SyntaxTree<SyntaxTree> {
    private Position position;
    private Context context;
    protected List<SyntaxTree> children;

    protected SyntaxTree(SyntaxTree... children) {
        this(Arrays.stream(children).filter(Objects::nonNull).toList());
    }

    protected SyntaxTree(List<SyntaxTree> children) {
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
    public List<SyntaxTree> children() {
        return children;
    }

    public abstract <T> T visit(Visitor<T> visitor);

    public interface Visitor<T> {
        T visitAssignStatement(AssignStatement statement);

        T visitBlockStatement(BlockStatement statement);

        T visitBranchStatement(BranchStatement statement);

        T visitClassStatement(ClassStatement statement);

        T visitDefineStatement(DefineStatement statement);

        T visitDiscardStatement(DiscardStatement statement);

        T visitDoubleOperatorExpression(DoubleOperatorExpression expression);

        T visitExportStatement(ExportStatement statement);

        T visitFunctionExpression(FunctionExpression expression);

        T visitFunctionStatement(FunctionStatement statement);

        T visitIdentifierExpression(ElementExpression expression);

        T visitImportStatement(ImportStatement statement);

        T visitIndexExpression(IndexExpression expression);

        T visitLiteralExpression(LiteralExpression expression);

        T visitReturnStatement(ReturnStatement statement);

        T visitSingleOperatorExpression(SingleOperatorExpression expression);

        T visitSource(Source source);

        T visitTraitStatement(TraitStatement statement);
    }
}
