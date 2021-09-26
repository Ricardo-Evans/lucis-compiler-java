package compiler.lexer;

import compiler.entity.Position;
import compiler.entity.Unit;
import compiler.io.Reader;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * Lexer implemented with deterministic finite automation.
 *
 * @author owl
 * @author Ricardo Evans
 * @version 1.1
 */
public class DFALexer implements Lexer {
    @Serial
    private static final long serialVersionUID = -4547807007049067905L;
    private final DFAState initialState;

    private DFALexer(DFAState initialState) {
        this.initialState = initialState;
    }

    @Override
    public Stream<Unit> resolve(Reader reader) {
        Objects.requireNonNull(reader);
        return Stream.generate(() -> {
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
                    throw new LexicalException("cannot recognize " + builder + " at " + position + " as a lexical unit");
                return new Unit(terminate.name, content, position);
            } catch (Exception e) {
                e.printStackTrace();
                throw new LexicalException(e);
            }
        }).takeWhile(Objects::nonNull);
    }

    private record Range(int start, int end) implements Serializable {
        @Serial
        private static final long serialVersionUID = -1515000752677691088L;
        public static final Range EMPTY = new Range(0, 0);

        public boolean hasSubset(Range range) {
            return this.start <= range.start && this.end >= range.end;
        }

        public boolean contains(Integer value) {
            if (value == null) return false;
            return start <= value && value < end;
        }

        public boolean isEmpty() {
            return end <= start;
        }

        public static Range of(int value) {
            return new Range(value, value + 1);
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + ")";
        }
    }

    private static class DFAState implements Serializable {
        @Serial
        private static final long serialVersionUID = -4570551895811702569L;
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

    private static class NFAState implements Serializable {
        @Serial
        private static final long serialVersionUID = 1876342921192679272L;
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

        private record Transfer(Range range, Set<NFAState> target) {
            public static final Transfer EMPTY = new Transfer(Range.EMPTY, Set.of());
        }

        public Builder() {
        }

        @Override
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
                    Iterator<Integer> iterator = expression.content().codePoints().iterator();
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
                    Range range = new Range(expression.start(), expression.end());
                    current.get(range).add(result);
                    return result;
                }

                @Override
                public NFAState visitConcatenation(RegularExpression.Concatenation expression) {
                    NFAState state = current;
                    current = new NFAState();
                    state.get(null).add(current);
                    for (RegularExpression e : expression.expressions()) {
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
                    for (RegularExpression e : expression.expressions()) {
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
                    NFAState state = expression.expression().visit(this);
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

        @Override
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

        /*
         * calculate the closure of nfa states
         */
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

        /*
         * calculate the movement of the closure
         */
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

        /*
         * called when calculating movement, optimize the given movement map to match following requirements:
         * 1. all keys in the movement has no intersections
         * 2. for any two movement in the movement map, either the keys are not continuous or the values are not same
         * 3. all values in the movement are the closure of themselves
         */
        private static Map<Range, Set<NFAState>> optimize(Map<Range, Set<NFAState>> movement) {
            PriorityQueue<Transfer> queue = new PriorityQueue<>(Comparator.comparing(Transfer::range, Comparator.comparingInt(Range::start)));
            movement.forEach((r, s) -> queue.offer(new Transfer(r, closure(s))));
            Transfer currentTransfer = Transfer.EMPTY;
            Map<Range, Set<NFAState>> result = new HashMap<>();
            while (!queue.isEmpty()) {
                Transfer transfer = queue.poll();
                Range currentRange = currentTransfer.range;
                Range range = transfer.range;
                Range intersection = new Range(Math.max(currentRange.start, range.start), Math.min(currentRange.end, range.end));
                if (intersection.isEmpty()) {
                    if (!currentRange.isEmpty()) {
                        if (currentRange.end == range.start && Objects.equals(currentTransfer.target, transfer.target))
                            currentTransfer = new Transfer(new Range(currentRange.start, range.end), transfer.target);
                        else {
                            result.put(currentRange, currentTransfer.target);
                            currentTransfer = transfer;
                        }
                    } else currentTransfer = transfer;
                } else {
                    Range left = new Range(currentRange.start, Math.min(currentRange.end, range.start));
                    if (!left.isEmpty()) result.put(left, currentTransfer.target);
                    Range right = new Range(Math.min(currentRange.end, range.end), Math.max(currentRange.end, range.end));
                    if (!right.isEmpty())
                        queue.offer(new Transfer(right, range.hasSubset(right) ? transfer.target : currentTransfer.target));
                    Set<NFAState> union = new HashSet<>();
                    union.addAll(currentTransfer.target);
                    union.addAll(transfer.target);
                    currentTransfer = new Transfer(intersection, union);
                }
            }
            if (!currentTransfer.range.isEmpty()) result.put(currentTransfer.range, currentTransfer.target);
            return result;
        }
    }
}
