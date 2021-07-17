package compiler.semantic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DefinePass {
    String value();

    Type type();

    boolean parallel() default false;

    enum Type {
        BFS,
        DFS,
    }
}
