package compiler.semantic;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Serializables.class)
public @interface Serializable {
    String field();
}
