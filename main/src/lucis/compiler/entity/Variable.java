package lucis.compiler.entity;

public interface Variable extends Symbol {
    Type getType();

    String getName();
}
