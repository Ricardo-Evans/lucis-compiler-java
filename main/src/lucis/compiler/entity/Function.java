package lucis.compiler.entity;

import java.util.List;

public interface Function {
    String getName();

    Type getResultType();

    List<Type> getParameterTypes();

    List<String> getParameterNames();
}
