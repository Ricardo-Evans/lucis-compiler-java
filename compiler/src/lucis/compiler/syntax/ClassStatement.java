package lucis.compiler.syntax;

import java.util.List;

public class ClassStatement extends Statement {
    public String name;
    public List<Field> fields;

    public ClassStatement(String name, List<Field> fields) {
        super();
        this.name = name;
        this.fields = fields;
    }

    public static class Field {
        public final String type;
        public final String name;

        public Field(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
        return visitor.visitClassStatement(this);
    }
}