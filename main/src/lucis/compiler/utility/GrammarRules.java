package lucis.compiler.utility;

import compiler.parser.GrammarRule;
import compiler.parser.GrammarPriority;
import lucis.compiler.syntax.*;
import lucis.compiler.utility.Utility;

import java.util.LinkedList;
import java.util.List;

@GrammarPriority(group = "expression", name = "index-expression", priority = 0)
@GrammarPriority(group = "expression", name = "bracket-expression", priority = 0)
@GrammarPriority(group = "expression", name = "element-expression", priority = 0)
@GrammarPriority(group = "expression", name = "literal-expression", priority = 0)
@GrammarPriority(group = "expression", name = "function-expression", priority = 0)
@GrammarPriority(group = "expression", name = "negation-expression", priority = 1)
@GrammarPriority(group = "expression", name = "negative-expression", priority = 1)
@GrammarPriority(group = "expression", name = "positive-expression", priority = 1)
@GrammarPriority(group = "expression", name = "division-expression", priority = 2)
@GrammarPriority(group = "expression", name = "multiply-expression", priority = 2)
@GrammarPriority(group = "expression", name = "remainder-expression", priority = 2)
@GrammarPriority(group = "expression", name = "plus-expression", priority = 3)
@GrammarPriority(group = "expression", name = "minus-expression", priority = 3)
@GrammarPriority(group = "expression", name = "compare-expression", priority = 4)
@GrammarPriority(group = "expression", name = "equality-expression", priority = 5)
@GrammarPriority(group = "expression", name = "and-expression", priority = 6)
@GrammarPriority(group = "expression", name = "or-expression", priority = 7)
public final class GrammarRules {
    private GrammarRules() {
    }

    @GrammarRule("statement-list:statement-list statement")
    public static List<Statement> statementList(List<Statement> statements, Statement statement) {
        statements.add(statement);
        return statements;
    }

    @GrammarRule("statement-list:")
    public static List<Statement> statementList() {
        return new LinkedList<>();
    }

    @GrammarRule("parameter-list:parameter-list , parameter")
    public static List<FunctionStatement.Parameter> parameterList(List<FunctionStatement.Parameter> parameters, FunctionStatement.Parameter parameter) {
        parameters.add(parameter);
        return parameters;
    }

    @GrammarRule("parameter-list:parameter")
    public static List<FunctionStatement.Parameter> parameterList(FunctionStatement.Parameter parameter) {
        return new LinkedList<>(List.of(parameter));
    }

    @GrammarRule("parameter-list:")
    public static List<FunctionStatement.Parameter> parameterList() {
        return new LinkedList<>();
    }

    @GrammarRule("expression-list:expression-list , expression")
    public static List<Expression> expressionList(List<Expression> expressions, Expression expression) {
        expressions.add(expression);
        return expressions;
    }

    @GrammarRule("expression-list:expression")
    public static List<Expression> expressionList(Expression expression) {
        return new LinkedList<>(List.of(expression));
    }

    @GrammarRule("expression-list:")
    public static List<Expression> expressionList() {
        return new LinkedList<>();
    }

    @GrammarRule("source:statement-list")
    public static Source source(List<Statement> statements) {
        return new Source(statements);
    }

    @GrammarRule("statement:block-statement")
    @GrammarRule("statement:assign-statement")
    @GrammarRule("statement:branch-statement")
    @GrammarRule("statement:define-statement")
    @GrammarRule("statement:export-statement")
    @GrammarRule("statement:import-statement")
    @GrammarRule("statement:return-statement")
    @GrammarRule("statement:discard-statement")
    @GrammarRule("statement:function-statement")
    @GrammarRule("statement:expression-statement")
    public static Statement statement(Statement statement) {
        return statement;
    }

    @GrammarRule("block-statement:{ statement-list }")
    public static BlockStatement blockStatement(List<Statement> statements) {
        return new BlockStatement(statements);
    }

    @GrammarRule(value = "assign-statement:identifier = expression", includeNames = {"identifier"})
    public static AssignStatement assignStatement(String identifier, Expression expression) {
        return new AssignStatement(identifier, expression);
    }

    @GrammarRule("branch-statement:if expression : statement")
    public static BranchStatement branchStatement(Expression condition, Statement positive) {
        return branchStatement(condition, positive, null);
    }

    @GrammarRule("branch-statement:if expression : statement else : statement")
    public static BranchStatement branchStatement(Expression condition, Statement positive, Statement negative) {
        return new BranchStatement(condition, positive, negative);
    }

    @GrammarRule(value = "define-statement:identifier identifier = expression", includeNames = {"identifier"})
    public static DefineStatement defineStatement(String type, String identifier, Expression value) {
        return new DefineStatement(type, identifier, value);
    }

    @GrammarRule(value = "define-statement:identifier : = expression", includeNames = {"identifier"})
    public static DefineStatement defineStatement(String identifier, Expression value) {
        return defineStatement(null, identifier, value);
    }

    @GrammarRule(value = "export-statement:export identifier", includeNames = {"identifier"})
    public static ExportStatement exportStatement(String identifier) {
        return new ExportStatement(identifier);
    }

    @GrammarRule(value = "import-statement:import identifier", includeNames = {"identifier"})
    public static ImportStatement importStatement(String identifier) {
        return new ImportStatement(identifier);
    }

    @GrammarRule("return-statement:return expression")
    public static ReturnStatement returnStatement(Expression expression) {
        return new ReturnStatement(expression);
    }

    @GrammarRule("discard-statement:_ = expression")
    public static DiscardStatement discardStatement(Expression expression) {
        return new DiscardStatement(expression);
    }

    @GrammarRule(value = "function-statement:identifier identifier ( parameter-list ) : expression", includeNames = {"identifier"})
    public static FunctionStatement functionStatement(String type, String identifier, List<FunctionStatement.Parameter> parameters, Expression expression) {
        return new FunctionStatement(type, identifier, parameters, new ReturnStatement(expression));
    }

    @GrammarRule(value = "function-statement:identifier identifier ( parameter-list ) : block-statement", includeNames = {"identifier"})
    public static FunctionStatement functionStatement(String type, String identifier, List<FunctionStatement.Parameter> parameters, Statement body) {
        return new FunctionStatement(type, identifier, parameters, body);
    }

    @GrammarRule(value = "parameter:identifier identifier", includeNames = {"identifier"})
    public static FunctionStatement.Parameter parameter(String type, String identifier) {
        return new FunctionStatement.Parameter(type, identifier);
    }

    @GrammarRule("index-expression:expression-0 [ expression ]")
    public static IndexExpression indexExpression(Expression array, Expression index) {
        return new IndexExpression(array, index);
    }

    @GrammarRule("bracket-expression:( expression )")
    public static Expression bracketExpression(Expression expression) {
        return expression;
    }

    @GrammarRule(value = "literal-expression:integer", includeNames = {"integer"})
    public static LiteralExpression integerExpression(String integer) {
        return new LiteralExpression(LiteralExpression.Type.INTEGER, integer);
    }

    @GrammarRule(value = "literal-expression:decimal", includeNames = {"decimal"})
    public static LiteralExpression decimalExpression(String decimal) {
        return new LiteralExpression(LiteralExpression.Type.DECIMAL, decimal);
    }

    @GrammarRule(value = "literal-expression:normal-string", includeNames = {"normal-string"})
    public static LiteralExpression normalStringExpression(String string) {
        return new LiteralExpression(LiteralExpression.Type.STRING, Utility.escape(string));
    }

    @GrammarRule(value = "literal-expression:raw-string", includeNames = {"raw-string"})
    public static LiteralExpression rawStringExpression(String string) {
        return new LiteralExpression(LiteralExpression.Type.STRING, string);
    }

    @GrammarRule("function-expression:expression-0 ( expression-list )")
    public static FunctionExpression functionExpression(Expression function, List<Expression> parameters) {
        return new FunctionExpression(function, parameters);
    }

    @GrammarRule(value = "element-expression:identifier", includeNames = {"identifier"})
    public static ElementExpression elementExpression(String identifier) {
        return new ElementExpression(identifier);
    }

    @GrammarRule(value = "element-expression:expression-0 . identifier", includeNames = {"identifier"})
    public static ElementExpression elementExpression(Expression parent, String identifier) {
        return new ElementExpression(parent, identifier);
    }

    @GrammarRule("negation-expression:! expression-1")
    @GrammarRule("negation-expression:not expression-1")
    public static SingleOperatorExpression negationExpression(Expression expression) {
        return new SingleOperatorExpression(OperatorExpression.Operator.NOT, expression);
    }

    @GrammarRule("negative-expression:- expression-1")
    public static SingleOperatorExpression negativeExpression(Expression expression) {
        return new SingleOperatorExpression(OperatorExpression.Operator.NEGATIVE, expression);
    }

    @GrammarRule("positive-expression:+ expression-1")
    public static SingleOperatorExpression positiveExpression(Expression expression) {
        return new SingleOperatorExpression(OperatorExpression.Operator.POSITIVE, expression);
    }

    @GrammarRule("division-expression:expression-2 / expression-1")
    public static DoubleOperatorExpression divisionExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.DIVISION, expression1, expression2);
    }

    @GrammarRule("multiply-expression:expression-2 * expression-1")
    public static DoubleOperatorExpression multiplyExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.MULTIPLY, expression1, expression2);
    }

    @GrammarRule("remainder-expression:expression-2 % expression-1")
    public static DoubleOperatorExpression remainderExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.REMAINDER, expression1, expression2);
    }

    @GrammarRule("plus-expression:expression-3 + expression-2")
    public static DoubleOperatorExpression plusExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.PLUS, expression1, expression2);
    }

    @GrammarRule("minus-expression:expression-3 - expression-2")
    public static DoubleOperatorExpression minusExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.MINUS, expression1, expression2);
    }

    @GrammarRule("compare-expression:expression-4 < expression-3")
    public static DoubleOperatorExpression lessExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.LESS, expression1, expression2);
    }

    @GrammarRule("compare-expression:expression-4 > expression-3")
    public static DoubleOperatorExpression greaterExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.GREATER, expression1, expression2);
    }

    @GrammarRule("compare-expression:expression-4 < = expression-3")
    public static DoubleOperatorExpression lessEqualExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.LESS_EQUAL, expression1, expression2);
    }

    @GrammarRule("compare-expression:expression-4 > = expression-3")
    public static DoubleOperatorExpression greaterEqualExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.GREATER, expression1, expression2);
    }

    @GrammarRule("equality-expression:expression-5 = = expression-4")
    public static DoubleOperatorExpression equalExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.EQUAL, expression1, expression2);
    }

    @GrammarRule("equality-expression:expression-5 ! = expression-4")
    public static DoubleOperatorExpression notEqualExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.NOT_EQUAL, expression1, expression2);
    }

    @GrammarRule("and-expression:expression-6 & expression-5")
    @GrammarRule("and-expression:expression-6 and expression-5")
    public static DoubleOperatorExpression andExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.AND, expression1, expression2);
    }

    @GrammarRule("or-expression:expression-7 | expression-6")
    @GrammarRule("or-expression:expression-7 or expression-6")
    public static DoubleOperatorExpression orExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.OR, expression1, expression2);
    }
}