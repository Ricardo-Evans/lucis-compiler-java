package lucis.compiler.syntax;

public class IntegerExpression implements Expression{
    public final String value;

    public IntegerExpression(String value) {
        this.value = value;
    }
}
