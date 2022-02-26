package lucis.compiler.semantic.concept;

import java.util.Arrays;

public class UnionType extends LucisType {
    private final LucisType[] types;

    public UnionType(LucisType... types) {
        super(formatName(types), formatSignature(types));
        this.types = types;
    }

    @Override
    public boolean match(LucisType type) {
        return Arrays.stream(types()).allMatch(t -> t.match(type));
    }

    @Override
    public boolean assignable(LucisType type) {
        return Arrays.stream(types()).allMatch(t -> t.assignable(type));
    }

    public LucisType[] types() {
        return types;
    }

    private static String formatName(LucisType... types) {
        return Arrays.stream(types).map(LucisType::name).reduce("", (s1, s2) -> s1 + "|" + s2);
    }

    private static String formatSignature(LucisType... types) {
        return Arrays.stream(types).map(LucisType::signature).reduce("", (s1, s2) -> s1 + "|" + s2);
    }
}
