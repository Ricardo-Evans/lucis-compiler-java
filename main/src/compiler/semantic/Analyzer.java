package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Analyzer<V> {
    private final List<List<? extends V>> passes;

    private Analyzer(List<List<? extends V>> passes) {
        this.passes = passes;
    }

    public void resolve(SyntaxTree<?, ? super V> root) {
        Queue<SyntaxTree<?, ? super V>> queue = new LinkedList<>();
        queue.offer(root);
        passes.forEach(pass -> {
            while (!queue.isEmpty()) {
                SyntaxTree<?, ? super V> t = queue.poll();
                Arrays.stream(t.children()).forEach(queue::offer);
                pass.forEach(t::visit);
            }
        });
    }

    public static class Builder<V> {
        private final List<List<? extends V>> passes = new LinkedList<>();

        public Builder() {
        }

        public Analyzer<V> build() {
            return new Analyzer<>(passes);
        }

        public Pass pass() {
            return new Pass();
        }

        public Builder<V> pass(List<? extends V> pass) {
            passes.add(pass);
            return this;
        }

        private class Pass {
            private final List<V> steps = new LinkedList<>();

            private Pass() {
            }

            public Builder<V> done() {
                passes.add(steps);
                return Builder.this;
            }

            public Pass step(V step) {
                steps.add(step);
                return this;
            }
        }
    }
}
