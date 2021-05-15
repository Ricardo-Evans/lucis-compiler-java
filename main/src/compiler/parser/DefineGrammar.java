package compiler.parser;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(DefineGrammars.class)
public @interface DefineGrammar {
    String value();

    String[] includeNames() default {};

    String[] excludeNames() default {};

    int[] includeIndexes() default {};

    int[] excludeIndexes() default {};
}
