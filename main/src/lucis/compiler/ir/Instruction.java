package lucis.compiler.ir;

public final class Instruction {
    public enum Operation {
        FIND,
        LOAD,
        STORE,
        CONSTANT,
        JUMP,
        BRANCH,
        INVOKE,
        RETURN,
        NATIVE,
    }

    public final Operation operation;
    public final long data;

    public Instruction(Operation operation, long data) {
        this.operation = operation;
        this.data = data;
    }
}
