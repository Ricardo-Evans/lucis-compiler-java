package lucis.compiler.entity;

import java.util.List;

public interface Function extends Symbol {
    String getName();

    Type getResultType();

    List<Type> getParameterTypes();

    List<String> getParameterNames();
}
