package lucis.compiler.syntax;

public class StringExpression implements Expression{
    public final String value;

    public StringExpression(String value) {
        this.value = value;
    }
}
