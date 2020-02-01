package lucis.compiler.parser;

import lucis.compiler.entity.DeclarationTree;
import lucis.compiler.entity.ExpressionTree;
import lucis.compiler.entity.StatementTree;
import lucis.compiler.entity.SyntaxTree;

public interface Parser {
    SyntaxTree parse();

    DeclarationTree parseDeclaration();

    StatementTree parseStatement();

    ExpressionTree parseExpression();
}
