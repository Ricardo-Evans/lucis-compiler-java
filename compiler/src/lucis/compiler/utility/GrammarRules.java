package lucis.compiler.utility;

import compiler.parser.GrammarRule;
import compiler.parser.GrammarPriority;
import lucis.compiler.syntax.*;

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

    @GrammarRule("source:module-header declaration-list")
    private static Source source(ModuleHeader header, List<Declaration> declarations) {
        return new Source(header, declarations);
    }

    @GrammarRule("module-header:module module-name")
    private static ModuleHeader moduleHeader(String name) {
        return new ModuleHeader(name);
    }

    @GrammarRule("module-header:module-header import unique-identifier")
    private static ModuleHeader importModule(ModuleHeader header, UniqueIdentifier name) {
        header.imports.add(name);
        return header;
    }

    @GrammarRule("module-header:module-header export unique-identifier")
    private static ModuleHeader exportModule(ModuleHeader header, UniqueIdentifier name) {
        header.exports.add(name);
        return header;
    }

    @GrammarRule("declaration:class-declaration")
    @GrammarRule("declaration:trait-declaration")
    @GrammarRule("declaration:constant-declaration")
    @GrammarRule("declaration:function-declaration")
    private static Declaration declaration(Declaration declaration) {
        return declaration;
    }

    @GrammarRule("statement:block-statement")
    @GrammarRule("statement:assign-statement")
    @GrammarRule("statement:branch-statement")
    @GrammarRule("statement:define-statement")
    @GrammarRule("statement:return-statement")
    @GrammarRule("statement:discard-statement")
    @GrammarRule("statement:expression-statement")
    private static Statement statement(Statement statement) {
        return statement;
    }

    @GrammarRule("block-statement:{ statement-list }")
    private static BlockStatement blockStatement(List<Statement> statements) {
        return new BlockStatement(statements);
    }

    @GrammarRule(value = "class-declaration:class identifier { field-list }", includeNames = {"identifier"})
    private static ClassDeclaration classDeclaration(String name, List<ClassDeclaration.Field> fields) {
        return new ClassDeclaration(name, fields);
    }

    @GrammarRule(value = "field:unique-identifier identifier", includeNames = {"identifier"})
    private static ClassDeclaration.Field field(UniqueIdentifier type, String name) {
        return new ClassDeclaration.Field(type, name);
    }

    @GrammarRule(value = "trait-declaration:trait identifier { }", includeNames = {"identifier"})
    private static TraitDeclaration traitDeclaration(String name) {
        return new TraitDeclaration(name, null, null); // TODO resolve more info
    }

    @GrammarRule(value = "function-declaration:unique-identifier identifier ( parameter-list ) : expression", includeNames = {"identifier"})
    private static FunctionDeclaration functionDeclaration(UniqueIdentifier type, String identifier, List<FunctionDeclaration.Parameter> parameters, Expression expression) {
        return new FunctionDeclaration(type, identifier, parameters, new ReturnStatement(expression));
    }

    @GrammarRule(value = "function-declaration:unique-identifier identifier ( parameter-list ) : block-statement", includeNames = {"identifier"})
    private static FunctionDeclaration functionDeclaration(UniqueIdentifier type, String identifier, List<FunctionDeclaration.Parameter> parameters, Statement body) {
        return new FunctionDeclaration(type, identifier, parameters, body);
    }

    @GrammarRule(value = "parameter:unique-identifier identifier", includeNames = {"identifier"})
    private static FunctionDeclaration.Parameter parameter(UniqueIdentifier type, String identifier) {
        return new FunctionDeclaration.Parameter(type, identifier);
    }


    @GrammarRule(value = "assign-statement:expression = expression")
    private static AssignStatement assignStatement(Expression left, Expression right) {
        return new AssignStatement(left, right);
    }

    @GrammarRule("branch-statement:if expression : statement")
    private static BranchStatement branchStatement(Expression condition, Statement positive) {
        return branchStatement(condition, positive, null);
    }

    @GrammarRule("branch-statement:if expression : statement else : statement")
    private static BranchStatement branchStatement(Expression condition, Statement positive, Statement negative) {
        return new BranchStatement(condition, positive, negative);
    }

    @GrammarRule(value = "define-statement:unique-identifier identifier = expression", includeNames = {"identifier"})
    private static DefineStatement defineStatement(UniqueIdentifier type, String identifier, Expression value) {
        return new DefineStatement(type, identifier, value);
    }

    @GrammarRule(value = "define-statement:identifier : = expression", includeNames = {"identifier"})
    private static DefineStatement defineStatement(String identifier, Expression value) {
        return defineStatement(null, identifier, value);
    }

    @GrammarRule("return-statement:return expression")
    private static ReturnStatement returnStatement(Expression expression) {
        return new ReturnStatement(expression);
    }

    @GrammarRule("discard-statement:_ = expression")
    private static DiscardStatement discardStatement(Expression expression) {
        return new DiscardStatement(expression);
    }

    @GrammarRule("index-expression:expression-0 [ expression ]")
    private static IndexExpression indexExpression(Expression array, Expression index) {
        return new IndexExpression(array, index);
    }

    @GrammarRule("bracket-expression:( expression )")
    private static Expression bracketExpression(Expression expression) {
        return expression;
    }

    @GrammarRule(value = "literal-expression:integer", includeNames = {"integer"})
    private static LiteralExpression integerExpression(String integer) {
        return new LiteralExpression(LiteralExpression.Type.INTEGER, integer);
    }

    @GrammarRule(value = "literal-expression:decimal", includeNames = {"decimal"})
    private static LiteralExpression decimalExpression(String decimal) {
        return new LiteralExpression(LiteralExpression.Type.DECIMAL, decimal);
    }

    @GrammarRule(value = "literal-expression:normal-string", includeNames = {"normal-string"})
    private static LiteralExpression normalStringExpression(String string) {
        return new LiteralExpression(LiteralExpression.Type.STRING, Utility.escape(string));
    }

    @GrammarRule(value = "literal-expression:raw-string", includeNames = {"raw-string"})
    private static LiteralExpression rawStringExpression(String string) {
        return new LiteralExpression(LiteralExpression.Type.STRING, string);
    }

    @GrammarRule("function-expression:expression-0 ( expression-list )")
    private static FunctionExpression functionExpression(Expression function, List<Expression> parameters) {
        return new FunctionExpression(function, parameters);
    }

    @GrammarRule(value = "element-expression:identifier", includeNames = {"identifier"})
    private static ElementExpression elementExpression(String identifier) {
        return new ElementExpression(identifier);
    }

    @GrammarRule(value = "element-expression:expression-0 . identifier", includeNames = {"identifier"})
    private static ElementExpression elementExpression(Expression parent, String identifier) {
        return new ElementExpression(parent, identifier);
    }

    @GrammarRule("negation-expression:! expression-1")
    @GrammarRule("negation-expression:not expression-1")
    private static UnaryOperatorExpression negationExpression(Expression expression) {
        return new UnaryOperatorExpression(OperatorExpression.Operator.NOT, expression);
    }

    @GrammarRule("negative-expression:- expression-1")
    private static UnaryOperatorExpression negativeExpression(Expression expression) {
        return new UnaryOperatorExpression(OperatorExpression.Operator.NEGATIVE, expression);
    }

    @GrammarRule("positive-expression:+ expression-1")
    private static UnaryOperatorExpression positiveExpression(Expression expression) {
        return new UnaryOperatorExpression(OperatorExpression.Operator.POSITIVE, expression);
    }

    @GrammarRule("division-expression:expression-2 / expression-1")
    private static BinaryOperatorExpression divisionExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.DIVISION, expression1, expression2);
    }

    @GrammarRule("multiply-expression:expression-2 * expression-1")
    private static BinaryOperatorExpression multiplyExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.MULTIPLY, expression1, expression2);
    }

    @GrammarRule("remainder-expression:expression-2 % expression-1")
    private static BinaryOperatorExpression remainderExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.REMAINDER, expression1, expression2);
    }

    @GrammarRule("plus-expression:expression-3 + expression-2")
    private static BinaryOperatorExpression plusExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.PLUS, expression1, expression2);
    }

    @GrammarRule("minus-expression:expression-3 - expression-2")
    private static BinaryOperatorExpression minusExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.MINUS, expression1, expression2);
    }

    @GrammarRule("compare-expression:expression-4 < expression-3")
    private static BinaryOperatorExpression lessExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.LESS, expression1, expression2);
    }

    @GrammarRule("compare-expression:expression-4 > expression-3")
    private static BinaryOperatorExpression greaterExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.GREATER, expression1, expression2);
    }

    @GrammarRule("compare-expression:expression-4 < = expression-3")
    private static BinaryOperatorExpression lessEqualExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.LESS_EQUAL, expression1, expression2);
    }

    @GrammarRule("compare-expression:expression-4 > = expression-3")
    private static BinaryOperatorExpression greaterEqualExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.GREATER, expression1, expression2);
    }

    @GrammarRule("equality-expression:expression-5 = = expression-4")
    private static BinaryOperatorExpression equalExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.EQUAL, expression1, expression2);
    }

    @GrammarRule("equality-expression:expression-5 ! = expression-4")
    private static BinaryOperatorExpression notEqualExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.NOT_EQUAL, expression1, expression2);
    }

    @GrammarRule("and-expression:expression-6 & expression-5")
    @GrammarRule("and-expression:expression-6 and expression-5")
    private static BinaryOperatorExpression andExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.AND, expression1, expression2);
    }

    @GrammarRule("or-expression:expression-7 | expression-6")
    @GrammarRule("or-expression:expression-7 or expression-6")
    private static BinaryOperatorExpression orExpression(Expression expression1, Expression expression2) {
        return new BinaryOperatorExpression(OperatorExpression.Operator.OR, expression1, expression2);
    }


    @GrammarRule("statement-list:statement-list statement")
    private static List<Statement> statementList(List<Statement> statements, Statement statement) {
        statements.add(statement);
        return statements;
    }

    @GrammarRule("statement-list:")
    private static List<Statement> statementList() {
        return new LinkedList<>();
    }

    @GrammarRule("declaration-list:declaration-list declaration")
    private static List<Declaration> declarationList(List<Declaration> declarations, Declaration declaration) {
        declarations.add(declaration);
        return declarations;
    }

    @GrammarRule("declaration-list:")
    private static List<Declaration> declarationList() {
        return new LinkedList<>();
    }

    @GrammarRule("parameter-list:parameter-list , parameter")
    private static List<FunctionDeclaration.Parameter> parameterList(List<FunctionDeclaration.Parameter> parameters, FunctionDeclaration.Parameter parameter) {
        parameters.add(parameter);
        return parameters;
    }

    @GrammarRule("parameter-list:parameter")
    private static List<FunctionDeclaration.Parameter> parameterList(FunctionDeclaration.Parameter parameter) {
        return new LinkedList<>(List.of(parameter));
    }

    @GrammarRule("parameter-list:")
    private static List<FunctionDeclaration.Parameter> parameterList() {
        return new LinkedList<>();
    }

    @GrammarRule("expression-list:expression-list , expression")
    private static List<Expression> expressionList(List<Expression> expressions, Expression expression) {
        expressions.add(expression);
        return expressions;
    }

    @GrammarRule("expression-list:expression")
    private static List<Expression> expressionList(Expression expression) {
        return new LinkedList<>(List.of(expression));
    }

    @GrammarRule("expression-list:")
    private static List<Expression> expressionList() {
        return new LinkedList<>();
    }

    @GrammarRule("field-list:")
    private static List<ClassDeclaration.Field> fieldList() {
        return new LinkedList<>();
    }

    @GrammarRule("field-list:field-list field")
    private static List<ClassDeclaration.Field> fieldList(List<ClassDeclaration.Field> fields, ClassDeclaration.Field field) {
        fields.add(field);
        return fields;
    }

    @GrammarRule(value = "module-name:identifier . module-name", includeNames = {"identifier"})
    private static String moduleName(String name, String child) {
        return name + '.' + child;
    }

    @GrammarRule(value = "module-name:identifier", includeNames = {"identifier"})
    private static String moduleName(String name) {
        return name;
    }

    @GrammarRule(value = "unique-identifier:module-name : _", includeNames = {"_"})
    @GrammarRule(value = "unique-identifier:module-name : identifier", includeNames = {"identifier"})
    private static UniqueIdentifier uniqueIdentifier(String module, String name) {
        return new UniqueIdentifier(module, name);
    }

    @GrammarRule(value = "unique-identifier:identifier", includeNames = {"identifier"})
    private static UniqueIdentifier uniqueIdentifier(String name) {
        return new UniqueIdentifier(name);
    }
}
