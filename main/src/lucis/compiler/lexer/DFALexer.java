package lucis.compiler.lexer;

import lucis.compiler.entity.Position;
import lucis.compiler.entity.SyntaxTree;
import lucis.compiler.entity.Unit;
import lucis.compiler.io.Reader;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lexer implemented with deterministic finite automation.
 *
 * @author owl
 * @author Ricardo Evans
 * @version 1.1
 */
public class DFALexer implements Lexer {
    private final DFAState initialState;

    private DFALexer(DFAState initialState) {
        this.initialState = initialState;
    }

    @Override
    public Supplier<Unit> resolve(Reader reader) {
        Objects.requireNonNull(reader);
        return () -> {
            try {
                if (!reader.available()) return null;
                DFAState state = initialState;
                DFAState terminate = null;
                String content = null;
                Position position = reader.position();
                StringBuilder builder = new StringBuilder();
                reader.mark();
                while (state != null) {
                    if (state.name != null) {
                        terminate = state;
                        content = builder.toString();
                        reader.mark();
                    }
                    Integer codepoint = reader.next();
                    if (codepoint == null) break;
                    state = state.handle(codepoint);
                    builder.appendCodePoint(codepoint);
                }
                reader.reset();
                if (terminate == null)
                    throw new LexicalException("cannot recognize " + builder.toString() + " at " + position + " as a lexical unit");
                return new Unit(terminate.name, content, position);
            } catch (Exception e) {
                e.printStackTrace();
                throw new LexicalException(e);
            }
        };
    }

    private static class Range implements Serializable, Comparable<Range> {
        private final int start;
        private final int end;

        public Range(int start, int end) {
            if (end < start)
                throw new IllegalArgumentException("range from " + start + " to " + end + " does not exist");
            this.start = start;
            this.end = end;
        }

        public boolean hasSubset(Range range) {
            return this.start <= range.start && this.end >= range.end;
        }

        public boolean contains(Integer value) {
            if (value == null) return false;
            return start <= value && value < end;
        }

        public static Range of(int value) {
            return new Range(value, value + 1);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Range range = (Range) o;
            return start == range.start &&
                    end == range.end;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + ")";
        }

        @Override
        public int compareTo(Range o) {
            return Double.compare((start + end) / 2.0, (o.start + o.end) / 2.0);
        }
    }

    private static class DFAState implements Serializable {
        private final Map<Range, DFAState> transfer = new HashMap<>();
        private String name = null;

        public DFAState handle(Integer character) {
            for (Map.Entry<Range, DFAState> entry : transfer.entrySet())
                if (entry.getKey().contains(character)) return entry.getValue();
            return null;
        }

        public DFAState get(Range key) {
            return transfer.get(key);
        }

        public void put(Range key, DFAState value) {
            transfer.put(key, value);
        }
    }

    private static class NFAState {
        private final Map<Range, Set<NFAState>> transfer = new HashMap<>();
        private String name = null;

        public Set<NFAState> get(Range key) {
            transfer.putIfAbsent(key, new HashSet<>());
            return transfer.get(key);
        }

        public Set<NFAState> put(Range key, Set<NFAState> value) {
            return transfer.put(key, value);
        }
    }

    /**
     * Builder used to construct a dfa lexer by defining lexical rules.
     */
    public static class Builder implements Lexer.Builder {
        private final NFAState initialState = new NFAState();
        private final Map<String, Integer> priorities = new HashMap<>();

        public Builder() {
        }

        public Builder define(RegularExpression expression, String name, int priority) {
            Objects.requireNonNull(expression);
            Objects.requireNonNull(name);
            NFAState state = expression.visit(new RegularExpression.Visitor<>() {
                private NFAState current = initialState;

                @Override
                public NFAState visitEmpty() {
                    NFAState state = new NFAState();
                    current.get(null).add(state);
                    return state;
                }

                @Override
                public NFAState visitAny() {
                    NFAState state = new NFAState();
                    Range wildcard = new Range(0, Integer.MAX_VALUE);
                    current.get(wildcard).add(state);
                    return state;
                }

                @Override
                public NFAState visitPure(RegularExpression.Pure expression) {
                    Iterator<Integer> iterator = expression.content.codePoints().iterator();
                    NFAState state = current;
                    while (iterator.hasNext()) {
                        Range range = Range.of(iterator.next());
                        NFAState next = new NFAState();
                        state.get(range).add(next);
                        state = next;
                    }
                    return state;
                }

                @Override
                public NFAState visitRange(RegularExpression.Range expression) {
                    NFAState result = new NFAState();
                    Range range = new Range(expression.start, expression.end);
                    current.get(range).add(result);
                    return result;
                }

                @Override
                public NFAState visitConcatenation(RegularExpression.Concatenation expression) {
                    NFAState state = current;
                    current = new NFAState();
                    state.get(null).add(current);
                    for (RegularExpression e : expression.expressions) {
                        current = e.visit(this);
                        NFAState empty = new NFAState();
                        current.get(null).add(empty);
                        current = empty;
                    }
                    NFAState result = current;
                    current = state;
                    return result;
                }

                @Override
                public NFAState visitAlternation(RegularExpression.Alternation expression) {
                    NFAState storage = current;
                    NFAState result = new NFAState();
                    for (RegularExpression e : expression.expressions) {
                        current = new NFAState();
                        storage.get(null).add(current);
                        NFAState state = e.visit(this);
                        state.get(null).add(result);
                    }
                    current = storage;
                    return result;
                }

                @Override
                public NFAState visitClosure(RegularExpression.Closure expression) {
                    NFAState result = new NFAState();
                    NFAState storage = current;
                    current = new NFAState();
                    storage.get(null).add(current);
                    storage.get(null).add(result);
                    NFAState state = expression.expression.visit(this);
                    state.get(null).add(result);
                    state.get(null).add(current);
                    current = storage;
                    return result;
                }
            });
            state.name = name;
            priorities.put(name, priority);
            return this;
        }

        public Builder define(RegularExpression expression, String name) {
            return define(expression, name, 0);
        }

        public Lexer build() {
            Map<Set<NFAState>, DFAState> stateMap = new HashMap<>();
            stateMap.put(Set.of(), null);
            DFAState state = new DFAState();
            Set<Set<NFAState>> remaining = new HashSet<>();
            Set<NFAState> initialClosure = closure(Set.of(initialState));
            remaining.add(initialClosure);
            stateMap.put(initialClosure, state);
            while (!remaining.isEmpty()) {
                Set<Set<NFAState>> changed = new HashSet<>();
                for (Set<NFAState> states : remaining) {
                    DFAState dfaState = stateMap.get(states);
                    Map<Range, Set<NFAState>> movement = move(states);
                    movement.forEach((r, s) -> {
                        if (!stateMap.containsKey(s)) {
                            changed.add(s);
                            stateMap.put(s, new DFAState());
                        }
                        dfaState.put(r, stateMap.get(s));
                    });
                    states.stream().filter(s -> s.name != null).forEach(s -> {
                        String name = s.name;
                        if (Objects.equals(priorities.get(name), priorities.get(dfaState.name)))
                            throw new LexicalException("lexical rules conflict between " + name + " and " + dfaState.name);
                        if (priorities.get(name) > priorities.getOrDefault(dfaState.name, Integer.MIN_VALUE))
                            dfaState.name = name;
                    });
                }
                remaining = changed;
            }
            return new DFALexer(state);
        }

        private static Set<NFAState> closure(Set<NFAState> states) {
            Set<NFAState> closure = null;
            boolean flag = true;
            while (flag) {
                closure = new HashSet<>(states);
                flag = false;
                for (NFAState state : states)
                    if (closure.addAll(state.get(null))) flag = true;
                states = closure;
            }
            return closure;
        }

        private static Map<Range, Set<NFAState>> move(Set<NFAState> states) {
            Map<Range, Set<NFAState>> movement = new HashMap<>();
            for (NFAState state : states) {
                state.transfer.forEach((r, s) -> {
                    if (r == null) return;
                    movement.putIfAbsent(r, new HashSet<>());
                    movement.get(r).addAll(s);
                });
            }
            return optimize(movement);
        }

        private static Map<Range, Set<NFAState>> optimize(Map<Range, Set<NFAState>> movement) {
            Set<Integer> boundaries = new TreeSet<>();
            movement.keySet().forEach(r -> {
                boundaries.add(r.start);
                boundaries.add(r.end);
            });
            Map<Range, Set<NFAState>> result = new TreeMap<>();
            movement.forEach((r, s) -> {
                int last = -1;
                for (int boundary : boundaries) {
                    Range range = new Range(last, boundary);
                    if (r.hasSubset(range)) {
                        result.putIfAbsent(range, new HashSet<>());
                        result.get(range).addAll(s);
                    }
                    last = boundary;
                }
            });
            result.replaceAll((r, s) -> closure(s));
            if (result.isEmpty()) return result;
            return merge(result);
        }

        private static Map<Range, Set<NFAState>> merge(Map<Range, Set<NFAState>> optimized) {
            optimized = new TreeMap<>(optimized);
            Iterator<Map.Entry<Range, Set<NFAState>>> iterator = optimized.entrySet().iterator();
            Map<Range, Set<NFAState>> result = new HashMap<>();
            Map.Entry<Range, Set<NFAState>> entry = iterator.next();
            Range range = entry.getKey();
            Set<NFAState> states = entry.getValue();
            while (iterator.hasNext()) {
                entry = iterator.next();
                if (entry.getKey().start == range.end && states.equals(entry.getValue()))
                    range = new Range(range.start, entry.getKey().end);
                else {
                    result.put(range, states);
                    range = entry.getKey();
                    states = entry.getValue();
                }
            }
            result.put(range, states);
            return result;
        }
    }
}
