package lucis.compiler.parser;

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
        lexemes.forEach(unit -> handle(unit, states, units));
        return units.pop();
    }

    private void handle(Unit unit, Deque<State> states, Deque<Unit> units) {
        State state = states.peek();
        assert state != null;
        Action action = state.handle(unit);
        if (action == null)
            throw new GrammaticalException("cannot handle '" + units.stream().map(Unit::name).reduce("", (s1, s2) -> s1 + " " + s2) + " ' as a grammatical structure");
        switch (action.type()) {
            case ACCEPT: {
                Grammar grammar = action.grammar();
                int length = grammar.length();
                Unit[] handle = new Unit[length];
                for (int i = 0; i < length; ++i) {
                    handle[length - i - 1] = units.pop();
                    states.pop();
                }
                units.push(grammar.reduction.reduce(handle));
                break;
            }
            case REDUCE: {
                Grammar grammar = action.grammar();
                int length = grammar.length();
                Unit[] handle = new Unit[length];
                for (int i = 0; i < length; ++i) {
                    handle[length - i - 1] = units.pop();
                    states.pop();
                }
                state = states.peek();
                assert state != null;
                Unit reduction = grammar.reduction.reduce(handle);
                units.push(reduction);
                states.push(state.handle(reduction).state());
                handle(unit, states, units);
                break;
            }
            case SHIFT: {
                states.push(action.state());
                units.push(unit);
                break;
            }
        }
    }

    private static class State {
        private final Map<String, Action> actionMap = new HashMap<>();

        public Action handle(Unit node) {
            return actionMap.get(node == null ? null : node.name());
        }
    }

    private static class Action {
        private final Type type;
        private final State state;
        private final Grammar grammar;

        enum Type {
            SHIFT,
            REDUCE,
            ACCEPT,
        }

        public Action(Type type, State state, Grammar grammar) {
            this.type = type;
            this.state = state;
            this.grammar = grammar;
        }

        public Type type() {
            return type;
        }

        public State state() {
            return state;
        }

        public Grammar grammar() {
            return grammar;
        }

        public static Action accept(Grammar grammar) {
            return new Action(Type.ACCEPT, null, grammar);
        }

        public static Action shift(State state) {
            return new Action(Type.SHIFT, state, null);
        }

        public static Action reduce(Grammar grammar) {
            return new Action(Type.REDUCE, null, grammar);
        }
    }

    public static class Builder implements Parser.Builder {
        private final Set<String> vt = new HashSet<>();
        private final Set<String> vn = new HashSet<>();
        private final Map<String, Set<Grammar>> grammars = new HashMap<>();
        private Map<String, Set<String>> peekMap;
        private final String goal;
        private final String empty;

        public Builder(String goal, String empty) {
            Objects.requireNonNull(goal, "the goal cannot be null");
            Objects.requireNonNull(empty, "the empty cannot be null");
            this.goal = goal;
            this.empty = empty;
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
                                actionMap.put(item.peek, Action.reduce(item.grammar));
                        } else {
                            movement.putIfAbsent(item.current(), new HashSet<>());
                            movement.get(item.current()).add(item.next());
                        }
                    }
                    movement.replaceAll((k, v) -> closure(v));
                    movement.forEach((symbol, ccy) -> {
                        if (!cc.containsKey(ccy)) {
                            cc.put(ccy, new State());
                            changed.add(ccy);
                        }
                        if (actionMap.containsKey(symbol)) conflictGrammars(ccx);
                        actionMap.put(symbol, Action.shift(cc.get(ccy)));
                    });
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
                        peekSet.add(empty);
                        for (String x : g.right) {
                            if (!peekSet.remove(empty)) break;
                            peekSet.addAll(peekMap.get(x));
                        }
                        if (peekMap.get(s).addAll(peekSet)) flag = true;
                    }
                }
            }
        }

        private Set<String> peek(Item item) {
            Set<String> peekSet = new HashSet<>();
            peekSet.add(empty);
            for (int i = item.index + 1; i < item.grammar.length(); ++i) {
                if (!peekSet.remove(empty)) break;
                peekSet.addAll(peekMap.get(item.grammar.right[i]));
            }
            if (peekSet.remove(empty)) peekSet.add(item.peek);
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
        }
    }
}
