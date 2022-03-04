package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

public interface Analyzer<T extends SyntaxTree<T, ?>, E> {
    void analyze(Collection<T> trees, E environment);

    interface Builder<T extends SyntaxTree<T, ?>, E> {
        Analyzer<T, E> build();

        Builder<T, E> definePass(Pass<T, E> pass);

        default Builder<T, E> definePasses(Class<?> passes) {
            for (Method method : passes.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(DefinePass.class)) continue;
                if (!Modifier.isStatic(method.getModifiers())) throw new SemanticException("method used to define analyze passes should be static");
                if (!method.canAccess(null)) method.setAccessible(true);
                definePass((tree, environment) -> {
                    try {
                        method.invoke(null, tree, environment);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new SemanticException(e);
                    }
                });
            }
            return this;
        }
    }
}
