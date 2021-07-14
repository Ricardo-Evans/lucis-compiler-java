package compiler.parser;

import compiler.entity.Position;
import compiler.entity.Unit;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FunctionalInterface
public interface Parser extends Serializable {
    <T> T parse(Stream<? extends Unit> lexemes);

    @FunctionalInterface
    interface Hook<T> {
        T hook(Object value, Position position);
    }

    interface Builder {
        Builder define(Grammar grammar);

        default Builder define(List<Grammar> grammars) {
            grammars.forEach(this::define);
            return this;
        }

        default Builder define(Class<?> grammars) {
            Map<String, Map<String, Integer>> priorities = new HashMap<>();
            if (grammars.isAnnotationPresent(GrammarPriority.class)) {
                GrammarPriority grammarPriority = grammars.getAnnotation(GrammarPriority.class);
                priorities.putIfAbsent(grammarPriority.group(), new HashMap<>());
                priorities.get(grammarPriority.group()).put(grammarPriority.name(), grammarPriority.priority());
            }
            if (grammars.isAnnotationPresent(GrammarPriorities.class)) {
                GrammarPriorities grammarPriorities = grammars.getAnnotation(GrammarPriorities.class);
                for (GrammarPriority grammarPriority : grammarPriorities.value()) {
                    priorities.putIfAbsent(grammarPriority.group(), new HashMap<>());
                    priorities.get(grammarPriority.group()).put(grammarPriority.name(), grammarPriority.priority());
                }
            }
            priorities.forEach(this::definePriorities);
            for (Method method : grammars.getMethods()) {
                if (method.isAnnotationPresent(GrammarRule.class)) {
                    GrammarRule grammarRule = method.getAnnotation(GrammarRule.class);
                    defineByAnnotation(grammarRule, method);
                }
                if (method.isAnnotationPresent(GrammarRules.class)) {
                    GrammarRules grammarRules = method.getAnnotation(GrammarRules.class);
                    for (GrammarRule grammarRule : grammarRules.value())
                        defineByAnnotation(grammarRule, method);
                }
            }
            return this;
        }

        default Parser build() {
            return build(null);
        }

        Parser build(Hook<?> hook);

        private void definePriorities(String group, Map<String, Integer> priorities) {
            List<Grammar> grammars = new LinkedList<>();
            priorities.forEach((s, i) -> grammars.add(new Grammar(group + "-" + i, new String[]{s}, handle -> handle[0])));
            Iterator<Integer> iterator = new HashSet<>(priorities.values()).stream().sorted(Comparator.reverseOrder()).iterator();
            String s0 = group;
            while (iterator.hasNext()) {
                String s = group + "-" + iterator.next();
                grammars.add(new Grammar(s0, new String[]{s}, handle -> handle[0]));
                s0 = s;
            }
            define(grammars);
        }

        private void defineByAnnotation(GrammarRule grammarRule, Method method) {
            if (!Modifier.isStatic(method.getModifiers()) || !method.canAccess(null))
                throw new GrammaticalException(); // TODO more specific exception
            String grammarString = grammarRule.value();
            int index = grammarString.indexOf(':');
            if (index < 0 || index >= grammarString.length())
                throw new IllegalArgumentException("grammar in wrong format: " + grammarString);
            String leftString = grammarString.substring(0, index);
            String[] rightString = splitStringByBlank(grammarString.substring(index + 1));
            Set<String> includeNames = Set.of(grammarRule.includeNames());
            Set<String> excludeNames = Set.of(grammarRule.excludeNames());
            Set<Integer> includeIndexes = Arrays.stream(grammarRule.includeIndexes()).boxed().collect(Collectors.toSet());
            Set<Integer> excludeIndexes = Arrays.stream(grammarRule.excludeIndexes()).boxed().collect(Collectors.toSet());
            int length = rightString.length;
            Grammar.Part[] rightParts = new Grammar.Part[length];
            for (int i = 0; i < length; ++i) {
                Grammar.Capture capture = Grammar.Capture.DEFAULT;
                String name = rightString[i];
                if (includeNames.contains(name)) capture = Grammar.Capture.INCLUDE;
                if (excludeNames.contains(name)) capture = Grammar.Capture.EXCLUDE;
                if (includeIndexes.contains(i)) capture = Grammar.Capture.INCLUDE;
                if (excludeIndexes.contains(i)) capture = Grammar.Capture.EXCLUDE;
                rightParts[i] = new Grammar.Part(name, capture);
            }
            Function<Object[], Object> reduction = wrapReduction(method);
            Grammar grammar = new Grammar(leftString, rightParts, reduction);
            define(grammar);
        }

        private static Function<Object[], Object> wrapReduction(Method method) {
            return handle -> {
                try {
                    return method.invoke(null, handle);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        }

        private static String[] splitStringByBlank(String s) {
            return Arrays.stream(s.split("\\s")).filter(Predicate.not(String::isBlank)).map(String::strip).toArray(String[]::new);
        }
    }
}
