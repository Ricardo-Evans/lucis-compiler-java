package compiler.parser;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(GrammarRules.class)
public @interface GrammarRule {
    String value();

    String[] includeNames() default {};

    String[] excludeNames() default {};

    int[] includeIndexes() default {};

    int[] excludeIndexes() default {};
}
