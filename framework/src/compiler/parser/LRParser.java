package compiler.parser;

import compiler.entity.Position;
import compiler.entity.Unit;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LRParser implements Parser {
    @Serial
    private static final long serialVersionUID = 7616064053119080359L;
    private final State initialState;

    private LRParser(State initialState) {
        this.initialState = initialState;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T parse(Stream<? extends Unit> lexemes) {
        Objects.requireNonNull(lexemes);
        Deque<State> states = new ArrayDeque<>();
        Deque<Unit> units = new ArrayDeque<>();
        states.push(initialState);
        lexemes.forEach(unit -> {
            State state = states.peek();
            assert state != null;
            Action action = state.handle(unit);
            if (action == null)
                throw new GrammaticalException("cannot handle '" + string(units) + "' before " + unit.position() + " as a grammatical structure");
            action.act(unit, units, states);
        });
        State state = states.peek();
        if (state == null)
            throw new GrammaticalException("remain " + string(units) + " cannot be recognized after parsing");
        Action action = state.handle(null);
        action.act(null, units, states);
        if (units.size() != 1 || states.size() != 1)
            throw new GrammaticalException("accident occur during parsing, remain " + string(units) + " not recognized");
        return (T) units.pop().value();
    }

    private static String string(Collection<? extends Unit> units) {
        List<String> names = units.stream().map(Unit::name).collect(Collectors.toList());
        Collections.reverse(names);
        return names.stream().reduce("", (s1, s2) -> s1 + " " + s2);
    }

    private record State(Map<String, Action> actionMap) implements Serializable {
        @Serial
        private static final long serialVersionUID = 7681443543917405304L;

        public State() {
            this(new HashMap<>());
        }

        public Action handle(Unit node) {
            return actionMap.get(node == null ? null : node.name());
        }
    }

    @FunctionalInterface
    private interface Action extends Serializable {
        void act(Unit unit, Deque<Unit> units, Deque<State> states);

        private static Action accept(Grammar grammar, Hook<?> hook) {
            return (unit, units, states) -> units.push(applyReduction(grammar, units, states, hook));
        }

        private static Action reduce(Grammar grammar, Hook<?> hook) {
            return (unit, units, states) -> {
                Unit reduced = applyReduction(grammar, units, states, hook);
                State state = states.peek();
                assert state != null;
                state.handle(reduced).act(reduced, units, states);
                state = states.peek();
                assert state != null;
                state.handle(unit).act(unit, units, states);
            };
        }

        private static Action shift(State state) {
            return (unit, units, states) -> {
                states.push(state);
                units.push(unit);
            };
        }

        private static Unit applyReduction(Grammar grammar, Deque<Unit> units, Deque<State> states, Hook<?> hook) {
            int length = grammar.length();
            Position position = null;
            List<Object> objects = new LinkedList<>();
            for (int i = 0; i < length; ++i) {
                Unit u = units.pop();
                position = u.position();
                if (grammar.right()[length - i - 1].capture == Grammar.Capture.INCLUDE)
                    objects.add(0, u.value());
                states.pop();
            }
            Object value = grammar.reduction().apply(objects.toArray());
            if (hook != null) value = hook.hook(value, position);
            return new Unit(grammar.left(), value, position);
        }
    }

    public static class Builder implements Parser.Builder {
        private final Set<String> vt = new HashSet<>();
        private final Set<String> vn = new HashSet<>();
        private final Set<Grammar> grammarSet = new HashSet<>();
        private final String goal;

        public Builder(String goal) {
            Objects.requireNonNull(goal, "the goal cannot be null");
            this.goal = goal;
        }

        @Override
        public Builder define(Grammar grammar) {
            Objects.requireNonNull(grammar, "the grammar cannot be null");
            grammarSet.add(grammar);
            vt.remove(grammar.left());
            vn.add(grammar.left());
            for (Grammar.Part p : grammar.right())
                if (!vn.contains(p.name)) vt.add(p.name);
            return this;
        }

        public Parser build(Hook<?> hook) {
            Map<String, Set<Grammar>> grammars = optimizeGrammar();
            Map<String, Set<String>> peekMap = calculatePeekMap(grammars);
            Map<Set<Item>, State> cc = new HashMap<>();
            Set<Set<Item>> remaining = new HashSet<>();
            Set<Item> cc0 = new HashSet<>();
            for (Grammar g : grammars.getOrDefault(goal, Set.of()))
                cc0.add(new Item(g, null));
            cc0 = closure(grammars, cc0, peekMap);
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
                            if (Objects.equals(item.grammar.left(), goal))
                                actionMap.put(item.peek, Action.accept(item.grammar, hook));
                            else
                                actionMap.put(item.peek, Action.reduce(item.grammar, hook));
                        } else {
                            movement.putIfAbsent(item.current(), new HashSet<>());
                            movement.get(item.current()).add(item.next());
                        }
                    }
                    movement.replaceAll((k, v) -> closure(grammars, v, peekMap));
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

        private Map<String, Set<Grammar>> optimizeGrammar() {
            Map<String, Set<Grammar>> optimizedGrammars = new HashMap<>();
            grammarSet.forEach(grammar -> {
                Grammar.Part[] rightPart = Arrays.stream(grammar.right()).map(part -> {
                    Grammar.Capture capture = part.capture;
                    if (capture == Grammar.Capture.DEFAULT) {
                        if (vn.contains(part.name)) capture = Grammar.Capture.INCLUDE;
                        if (vt.contains(part.name)) capture = Grammar.Capture.EXCLUDE;
                    }
                    return new Grammar.Part(part.name, capture);
                }).toArray(Grammar.Part[]::new);
                Grammar optimizedGrammar = new Grammar(grammar.left(), rightPart, grammar.reduction());
                optimizedGrammars.putIfAbsent(optimizedGrammar.left(), new HashSet<>());
                optimizedGrammars.get(optimizedGrammar.left()).add(optimizedGrammar);
            });
            return optimizedGrammars;
        }

        private Map<String, Set<String>> calculatePeekMap(Map<String, Set<Grammar>> grammars) {
            Map<String, Set<String>> peekMap = new HashMap<>();
            vt.forEach(s -> peekMap.put(s, Set.of(s)));
            vn.forEach(s -> peekMap.put(s, new HashSet<>()));
            boolean flag = true;
            while (flag) {
                flag = false;
                for (String s : vn) {
                    for (Grammar g : grammars.get(s)) {
                        Set<String> peekSet = new HashSet<>();
                        peekSet.add(null);
                        for (Grammar.Part p : g.right()) {
                            if (!peekSet.remove(null)) break;
                            peekSet.addAll(peekMap.get(p.name));
                        }
                        if (peekMap.get(s).addAll(peekSet)) flag = true;
                    }
                }
            }
            return peekMap;
        }

        private Set<String> peek(Item item, Map<String, Set<String>> peekMap) {
            Set<String> peekSet = new HashSet<>();
            peekSet.add(null);
            for (int i = item.index + 1; i < item.grammar.length(); ++i) {
                if (!peekSet.remove(null)) break;
                peekSet.addAll(peekMap.get(item.grammar.right()[i].name));
            }
            if (peekSet.remove(null)) peekSet.add(item.peek);
            return peekSet;
        }

        private Set<Item> closure(Map<String, Set<Grammar>> grammars, Set<Item> ccx, Map<String, Set<String>> peekMap) {
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
                        for (String peek : peek(item, peekMap))
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

        private record Item(Grammar grammar, String peek, int index) {
            public Item(Grammar grammar, String peek) {
                this(grammar, peek, 0);
            }

            private String current() {
                return grammar.right()[index].name;
            }

            private Item next() {
                return new Item(grammar, peek, index + 1);
            }

            private boolean isComplete() {
                return index >= grammar.right().length;
            }

            private boolean isPotential() {
                return index <= 0;
            }

            private boolean isPartial() {
                return !isComplete() && !isPotential();
            }
        }
    }
}
