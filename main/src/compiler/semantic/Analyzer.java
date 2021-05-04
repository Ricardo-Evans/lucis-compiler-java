package compiler.semantic;

import compiler.entity.SyntaxTree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Analyzer<V> {
    private final List<V> passes;

    public Analyzer(List<V> passes) {
        this.passes = passes;
    }

    public void resolve(SyntaxTree<?, V> root) {
        Queue<SyntaxTree<?, V>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            SyntaxTree<?, V> t = queue.poll();
            Arrays.stream(t.children()).forEach(queue::offer);
            passes.forEach(t::visit);
        }
    }
}
