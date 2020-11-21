package lucis.compiler.parser;

import lucis.compiler.entity.Unit;

@FunctionalInterface
public interface Reduction {
    Unit reduce(Unit... nodes);
}
