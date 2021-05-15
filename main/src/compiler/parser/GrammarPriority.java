package compiler.parser;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Repeatable(GrammarPriorities.class)
public @interface GrammarPriority {
    String group();

    String name();

    int priority();
}
