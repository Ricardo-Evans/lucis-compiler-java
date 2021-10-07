package lucis.compiler.syntax;

import java.util.List;

public class ClassDeclaration extends Declaration {
    public String name;
    public List<Field> fields;

    public ClassDeclaration(String name, List<Field> fields) {
        super();
        this.name = name;
        this.fields = fields;
    }

    public static class Field {
        public final UniqueIdentifier type;
        public final String name;

        public Field(UniqueIdentifier type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitClassStatement(this);
    }
}
