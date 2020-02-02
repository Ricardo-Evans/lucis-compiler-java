package lucis.compiler.entity;

import java.util.List;

public interface Type extends Symbol {
    boolean isVirtual();

    Type getBaseClass();

    List<Type> getTraits();

    String getName();

    List<Function> getMethods();

    List<Variable> getFields();
}
