package lucis.compiler.entity;

import lucis.compiler.utility.Name;

@Name("function-statement")
public class FunctionStatement extends Statement {
    public FunctionStatement(Statement statement) {
        super(statement);
    }
}
