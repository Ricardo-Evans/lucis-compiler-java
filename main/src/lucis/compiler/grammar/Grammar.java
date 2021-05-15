package lucis.compiler.grammar;

import compiler.parser.DefineGrammar;
import compiler.parser.DefinePriority;
import lucis.compiler.syntax.*;
import lucis.compiler.utility.Utility;

import java.util.LinkedList;
import java.util.List;

@DefinePriority(group = "expression", name = "index-expression", priority = 0)
@DefinePriority(group = "expression", name = "bracket-expression", priority = 0)
@DefinePriority(group = "expression", name = "element-expression", priority = 0)
@DefinePriority(group = "expression", name = "literal-expression", priority = 0)
@DefinePriority(group = "expression", name = "function-expression", priority = 0)
@DefinePriority(group = "expression", name = "negation-expression", priority = 1)
@DefinePriority(group = "expression", name = "negative-expression", priority = 1)
@DefinePriority(group = "expression", name = "positive-expression", priority = 1)
@DefinePriority(group = "expression", name = "division-expression", priority = 2)
@DefinePriority(group = "expression", name = "multiply-expression", priority = 2)
@DefinePriority(group = "expression", name = "remainder-expression", priority = 2)
@DefinePriority(group = "expression", name = "plus-expression", priority = 3)
@DefinePriority(group = "expression", name = "minus-expression", priority = 3)
@DefinePriority(group = "expression", name = "compare-expression", priority = 4)
@DefinePriority(group = "expression", name = "equality-expression", priority = 5)
@DefinePriority(group = "expression", name = "and-expression", priority = 6)
@DefinePriority(group = "expression", name = "or-expression", priority = 7)
public final class Grammar {
    private Grammar() {
    }

    @DefineGrammar("statement-list:statement-list statement")
    public static List<Statement> statementList(List<Statement> statements, Statement statement) {
        statements.add(statement);
        return statements;
    }

    @DefineGrammar("statement-list:")
    public static List<Statement> statementList() {
        return new LinkedList<>();
    }

    @DefineGrammar("parameter-list:parameter-list , parameter")
    public static List<FunctionStatement.Parameter> parameterList(List<FunctionStatement.Parameter> parameters, FunctionStatement.Parameter parameter) {
        parameters.add(parameter);
        return parameters;
    }

    @DefineGrammar("parameter-list:parameter")
    public static List<FunctionStatement.Parameter> parameterList(FunctionStatement.Parameter parameter) {
        return new LinkedList<>(List.of(parameter));
    }

    @DefineGrammar("parameter-list:")
    public static List<FunctionStatement.Parameter> parameterList() {
        return new LinkedList<>();
    }

    @DefineGrammar("expression-list:expression-list , expression")
    public static List<Expression> expressionList(List<Expression> expressions, Expression expression) {
        expressions.add(expression);
        return expressions;
    }

    @DefineGrammar("expression-list:expression")
    public static List<Expression> expressionList(Expression expression) {
        return new LinkedList<>(List.of(expression));
    }

    @DefineGrammar("expression-list:")
    public static List<Expression> expressionList() {
        return new LinkedList<>();
    }

    @DefineGrammar("source:statement-list")
    public static Source source(List<Statement> statements) {
        return new Source(statements);
    }

    @DefineGrammar("statement:block-statement")
    @DefineGrammar("statement:assign-statement")
    @DefineGrammar("statement:branch-statement")
    @DefineGrammar("statement:define-statement")
    @DefineGrammar("statement:export-statement")
    @DefineGrammar("statement:import-statement")
    @DefineGrammar("statement:return-statement")
    @DefineGrammar("statement:discard-statement")
    @DefineGrammar("statement:function-statement")
    @DefineGrammar("statement:expression-statement")
    public static Statement statement(Statement statement) {
        return statement;
    }

    @DefineGrammar("block-statement:{ statement-list }")
    public static BlockStatement blockStatement(List<Statement> statements) {
        return new BlockStatement(statements);
    }

    @DefineGrammar(value = "assign-statement:identifier = expression", includeNames = {"identifier"})
    public static AssignStatement assignStatement(String identifier, Expression expression) {
        return new AssignStatement(identifier, expression);
    }

    @DefineGrammar("branch-statement:if expression : statement")
    public static BranchStatement branchStatement(Expression condition, Statement positive) {
        return branchStatement(condition, positive, null);
    }

    @DefineGrammar("branch-statement:if expression : statement else : statement")
    public static BranchStatement branchStatement(Expression condition, Statement positive, Statement negative) {
        return new BranchStatement(condition, positive, negative);
    }

    @DefineGrammar(value = "define-statement:identifier identifier = expression", includeNames = {"identifier"})
    public static DefineStatement defineStatement(String type, String identifier, Expression value) {
        return new DefineStatement(type, identifier, value);
    }

    @DefineGrammar(value = "define-statement:identifier : = expression", includeNames = {"identifier"})
    public static DefineStatement defineStatement(String identifier, Expression value) {
        return defineStatement(null, identifier, value);
    }

    @DefineGrammar(value = "export-statement:export identifier", includeNames = {"identifier"})
    public static ExportStatement exportStatement(String identifier) {
        return new ExportStatement(identifier);
    }

    @DefineGrammar(value = "import-statement:import identifier", includeNames = {"identifier"})
    public static ImportStatement importStatement(String identifier) {
        return new ImportStatement(identifier);
    }

    @DefineGrammar("return-statement:return expression")
    public static ReturnStatement returnStatement(Expression expression) {
        return new ReturnStatement(expression);
    }

    @DefineGrammar("discard-statement:_ = expression")
    public static DiscardStatement discardStatement(Expression expression) {
        return new DiscardStatement(expression);
    }

    @DefineGrammar(value = "function-statement:identifier identifier ( parameter-list ) : expression", includeNames = {"identifier"})
    public static FunctionStatement functionStatement(String type, String identifier, List<FunctionStatement.Parameter> parameters, Expression expression) {
        return new FunctionStatement(type, identifier, parameters, new ReturnStatement(expression));
    }

    @DefineGrammar(value = "function-statement:identifier identifier ( parameter-list ) : block-statement", includeNames = {"identifier"})
    public static FunctionStatement functionStatement(String type, String identifier, List<FunctionStatement.Parameter> parameters, Statement body) {
        return new FunctionStatement(type, identifier, parameters, body);
    }

    @DefineGrammar(value = "parameter:identifier identifier", includeNames = {"identifier"})
    public static FunctionStatement.Parameter parameter(String type, String identifier) {
        return new FunctionStatement.Parameter(type, identifier);
    }

    @DefineGrammar("index-expression:expression-0 [ expression ]")
    public static IndexExpression indexExpression(Expression array, Expression index) {
        return new IndexExpression(array, index);
    }

    @DefineGrammar("bracket-expression:( expression )")
    public static Expression bracketExpression(Expression expression) {
        return expression;
    }

    @DefineGrammar(value = "literal-expression:integer", includeNames = {"integer"})
    public static LiteralExpression integerExpression(String integer) {
        return new LiteralExpression(LiteralExpression.Type.INTEGER, integer);
    }

    @DefineGrammar(value = "literal-expression:decimal", includeNames = {"decimal"})
    public static LiteralExpression decimalExpression(String decimal) {
        return new LiteralExpression(LiteralExpression.Type.DECIMAL, decimal);
    }

    @DefineGrammar(value = "literal-expression:normal-string", includeNames = {"normal-string"})
    public static LiteralExpression normalStringExpression(String string) {
        return new LiteralExpression(LiteralExpression.Type.STRING, Utility.escape(string));
    }

    @DefineGrammar(value = "literal-expression:raw-string", includeNames = {"raw-string"})
    public static LiteralExpression rawStringExpression(String string) {
        return new LiteralExpression(LiteralExpression.Type.STRING, string);
    }

    @DefineGrammar("function-expression:expression-0 ( expression-list )")
    public static FunctionExpression functionExpression(Expression function, List<Expression> parameters) {
        return new FunctionExpression(function, parameters);
    }

    @DefineGrammar(value = "element-expression:identifier", includeNames = {"identifier"})
    public static ElementExpression elementExpression(String identifier) {
        return new ElementExpression(identifier);
    }

    @DefineGrammar(value = "element-expression:expression-0 . identifier", includeNames = {"identifier"})
    public static ElementExpression elementExpression(Expression parent, String identifier) {
        return new ElementExpression(parent, identifier);
    }

    @DefineGrammar("negation-expression:! expression-1")
    @DefineGrammar("negation-expression:not expression-1")
    public static SingleOperatorExpression negationExpression(Expression expression) {
        return new SingleOperatorExpression(OperatorExpression.Operator.NOT, expression);
    }

    @DefineGrammar("negative-expression:- expression-1")
    public static SingleOperatorExpression negativeExpression(Expression expression) {
        return new SingleOperatorExpression(OperatorExpression.Operator.NEGATIVE, expression);
    }

    @DefineGrammar("positive-expression:+ expression-1")
    public static SingleOperatorExpression positiveExpression(Expression expression) {
        return new SingleOperatorExpression(OperatorExpression.Operator.POSITIVE, expression);
    }

    @DefineGrammar("division-expression:expression-2 / expression-1")
    public static DoubleOperatorExpression divisionExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.DIVISION, expression1, expression2);
    }

    @DefineGrammar("multiply-expression:expression-2 * expression-1")
    public static DoubleOperatorExpression multiplyExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.MULTIPLY, expression1, expression2);
    }

    @DefineGrammar("remainder-expression:expression-2 % expression-1")
    public static DoubleOperatorExpression remainderExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.REMAINDER, expression1, expression2);
    }

    @DefineGrammar("plus-expression:expression-3 + expression-2")
    public static DoubleOperatorExpression plusExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.PLUS, expression1, expression2);
    }

    @DefineGrammar("minus-expression:expression-3 - expression-2")
    public static DoubleOperatorExpression minusExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.MINUS, expression1, expression2);
    }

    @DefineGrammar("compare-expression:expression-4 < expression-3")
    public static DoubleOperatorExpression lessExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.LESS, expression1, expression2);
    }

    @DefineGrammar("compare-expression:expression-4 > expression-3")
    public static DoubleOperatorExpression greaterExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.GREATER, expression1, expression2);
    }

    @DefineGrammar("compare-expression:expression-4 < = expression-3")
    public static DoubleOperatorExpression lessEqualExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.LESS_EQUAL, expression1, expression2);
    }

    @DefineGrammar("compare-expression:expression-4 > = expression-3")
    public static DoubleOperatorExpression greaterEqualExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.GREATER, expression1, expression2);
    }

    @DefineGrammar("equality-expression:expression-5 = = expression-4")
    public static DoubleOperatorExpression equalExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.EQUAL, expression1, expression2);
    }

    @DefineGrammar("equality-expression:expression-5 ! = expression-4")
    public static DoubleOperatorExpression notEqualExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.NOT_EQUAL, expression1, expression2);
    }

    @DefineGrammar("and-expression:expression-6 & expression-5")
    @DefineGrammar("and-expression:expression-6 and expression-5")
    public static DoubleOperatorExpression andExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.AND, expression1, expression2);
    }

    @DefineGrammar("or-expression:expression-7 | expression-6")
    @DefineGrammar("or-expression:expression-7 or expression-6")
    public static DoubleOperatorExpression orExpression(Expression expression1, Expression expression2) {
        return new DoubleOperatorExpression(OperatorExpression.Operator.OR, expression1, expression2);
    }
}
