package lucis.compiler.parser;

import lucis.compiler.entity.Unit;

@FunctionalInterface
public interface Reduction {
    Object reduce(Unit... nodes);
}
