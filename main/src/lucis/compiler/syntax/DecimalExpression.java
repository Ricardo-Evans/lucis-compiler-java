package lucis.compiler.syntax;

public class DecimalExpression implements Expression{
    public final String value;

    public DecimalExpression(String value) {
        this.value = value;
    }
}
