package compiler.parser;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Repeatable(DefinePriorities.class)
public @interface DefinePriority {
    String group();

    String name();

    int priority();
}
