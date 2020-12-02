package lucis.compiler.parser;

import lucis.compiler.entity.Position;
import lucis.compiler.entity.Unit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LRParser implements Parser {
    private final State initialState;

    private LRParser(State initialState) {
        this.initialState = initialState;
    }

    @Override
    public Unit parse(Stream<? extends Unit> lexemes) {
        Objects.requireNonNull(lexemes);
        Deque<State> states = new ArrayDeque<>();
        Deque<Unit> units = new ArrayDeque<>();
        states.push(initialState);
        lexemes.forEach(unit -> {
            try {
                State state = states.peek();
                assert state != null;
                Action action = state.handle(unit);
                if (action == null)
                    throw new GrammaticalException("cannot handle '" + string(units) + "' as a grammatical structure");
                action.act(unit, units, states);
            } catch (Exception e) {
                throw new GrammaticalException(e);
            }
        });
        State state = states.peek();
        if (state == null)
            throw new GrammaticalException("remain " + string(units) + " cannot be recognized after parsing");
        Action action = states.peek().handle(null);
        action.act(null, units, states);
        if (units.size() != 1 || states.size() != 1)
            throw new GrammaticalException("accident occur during parsing, remain " + string(units) + " not recognized");
        return units.pop();
    }

    private static String string(Collection<? extends Unit> units) {
        List<String> names = units.stream().map(Unit::name).collect(Collectors.toList());
        Collections.reverse(names);
        return names.stream().reduce("", (s1, s2) -> s1 + " " + s2);
    }

    private static class State {
        private final Map<String, Action> actionMap = new HashMap<>();

        public Action handle(Unit node) {
            return actionMap.get(node == null ? null : node.name());
        }
    }

    @FunctionalInterface
    private interface Action {
        void act(Unit unit, Deque<Unit> units, Deque<State> states);

        static Unit reduce(Grammar grammar, Deque<Unit> units, Deque<State> states) {
            int length = grammar.length();
            Unit[] handle = new Unit[length];
            Position position = null;
            for (int i = 0; i < length; ++i) {
                Unit u = units.pop();
                position = u.position();
                handle[length - i - 1] = u;
                states.pop();
            }
            return new Unit(grammar.left, grammar.handler.apply(handle), position);
        }

        static Action accept(Grammar grammar) {
            return (unit, units, states) -> units.push(reduce(grammar, units, states));
        }

        static Action reduction(Grammar grammar) {
            return (unit, units, states) -> {
                Unit reduction = reduce(grammar, units, states);
                State state = states.peek();
                assert state != null;
                state.handle(reduction).act(reduction, units, states);
                state = states.peek();
                assert state != null;
                state.handle(unit).act(unit, units, states);
            };
        }

        static Action shift(State state) {
            return (unit, units, states) -> {
                states.push(state);
                units.push(unit);
            };
        }
    }

    public static class Builder implements Parser.Builder {
        private final Set<String> vt = new HashSet<>();
        private final Set<String> vn = new HashSet<>();
        private final Map<String, Set<Grammar>> grammars = new HashMap<>();
        private Map<String, Set<String>> peekMap;
        private final String goal;

        public Builder(String goal) {
            Objects.requireNonNull(goal, "the goal cannot be null");
            this.goal = goal;
        }

        public Builder define(Grammar grammar) {
            Objects.requireNonNull(grammar, "the grammar cannot be null");
            grammars.putIfAbsent(grammar.left, new HashSet<>());
            grammars.get(grammar.left).add(grammar);
            vt.remove(grammar.left);
            vn.add(grammar.left);
            for (String s : grammar.right)
                if (!vn.contains(s)) vt.add(s);
            return this;
        }

        public Parser build() {
            calculatePeekMap();
            Map<Set<Item>, State> cc = new HashMap<>();
            Set<Set<Item>> remaining = new HashSet<>();
            Set<Item> cc0 = new HashSet<>();
            for (Grammar g : grammars.getOrDefault(goal, Set.of()))
                cc0.add(new Item(g, null));
            cc0 = closure(cc0);
            remaining.add(cc0);
            cc.put(cc0, new State());
            while (!remaining.isEmpty()) {
                Set<Set<Item>> changed = new HashSet<>();
                for (Set<Item> ccx : remaining) {
                    Map<String, Set<Item>> movement = new HashMap<>();
                    Map<String, Action> actionMap = cc.get(ccx).actionMap;
                    for (Item item : ccx) {
                        if (item.isComplete()) {
                            if (actionMap.containsKey(item.peek)) conflictGrammars(ccx);
                            if (Objects.equals(item.grammar.left, goal))
                                actionMap.put(item.peek, Action.accept(item.grammar));
                            else
                                actionMap.put(item.peek, Action.reduction(item.grammar));
                        } else {
                            movement.putIfAbsent(item.current(), new HashSet<>());
                            movement.get(item.current()).add(item.next());
                        }
                    }
                    movement.replaceAll((k, v) -> closure(v));
                    Map<String, Action> shiftMap = new HashMap<>();
                    movement.forEach((symbol, ccy) -> {
                        if (!cc.containsKey(ccy)) {
                            cc.put(ccy, new State());
                            changed.add(ccy);
                        }
                        if (shiftMap.containsKey(symbol)) conflictGrammars(ccx);
                        shiftMap.put(symbol, Action.shift(cc.get(ccy)));
                    });
                    actionMap.putAll(shiftMap);
                }
                remaining = changed;
            }
            return new LRParser(cc.get(cc0));
        }

        private void calculatePeekMap() {
            peekMap = new HashMap<>();
            vt.forEach(s -> peekMap.put(s, Set.of(s)));
            vn.forEach(s -> peekMap.put(s, new HashSet<>()));
            boolean flag = true;
            while (flag) {
                flag = false;
                for (String s : vn) {
                    for (Grammar g : grammars.get(s)) {
                        Set<String> peekSet = new HashSet<>();
                        peekSet.add(null);
                        for (String x : g.right) {
                            if (!peekSet.remove(null)) break;
                            peekSet.addAll(peekMap.get(x));
                        }
                        if (peekMap.get(s).addAll(peekSet)) flag = true;
                    }
                }
            }
        }

        private Set<String> peek(Item item) {
            Set<String> peekSet = new HashSet<>();
            peekSet.add(null);
            for (int i = item.index + 1; i < item.grammar.length(); ++i) {
                if (!peekSet.remove(null)) break;
                peekSet.addAll(peekMap.get(item.grammar.right[i]));
            }
            if (peekSet.remove(null)) peekSet.add(item.peek);
            return peekSet;
        }

        private Set<Item> closure(Set<Item> ccx) {
            Set<Item> closure = null;
            boolean flag = true;
            while (flag) {
                flag = false;
                closure = new HashSet<>(ccx);
                for (Item item : ccx) {
                    if (item.isComplete()) continue;
                    String current = item.current();
                    if (vt.contains(current)) continue;
                    for (Grammar g : grammars.get(current))
                        for (String peek : peek(item))
                            if (closure.add(new Item(g, peek))) flag = true;
                }
                ccx = closure;
            }
            return closure;
        }

        private void conflictGrammars(Set<Item> ccx) {
            throw new GrammaticalException("conflict in grammars: "
                    + ccx.stream().map(item -> item.grammar.toString())
                    .collect(Collectors.toSet()).stream()
                    .reduce("", (s1, s2) -> s1 + "\n" + s2));
        }

        private static class Item {
            private final Grammar grammar;
            private final String peek;
            private final int index;

            public Item(Grammar grammar, String peek) {
                this(grammar, peek, 0);
            }

            private Item(Grammar grammar, String peek, int index) {
                this.grammar = grammar;
                this.peek = peek;
                this.index = index;
            }

            private String current() {
                return grammar.right[index];
            }

            private Item next() {
                return new Item(grammar, peek, index + 1);
            }

            private boolean isComplete() {
                return index >= grammar.right.length;
            }

            private boolean isPotential() {
                return index <= 0;
            }

            private boolean isPartial() {
                return !isComplete() && !isPotential();
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Item item = (Item) o;
                return index == item.index &&
                        Objects.equals(grammar, item.grammar) &&
                        Objects.equals(peek, item.peek);
            }

            @Override
            public int hashCode() {
                return Objects.hash(grammar, peek, index);
            }

            @Override
            public String toString() {
                return "Item{" +
                        "grammar=" + grammar +
                        ", peek='" + peek + '\'' +
                        ", index=" + index +
                        '}';
            }
        }
    }
}
