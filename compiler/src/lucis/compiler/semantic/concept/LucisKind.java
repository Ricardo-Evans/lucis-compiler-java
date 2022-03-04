package lucis.compiler.semantic.concept;

import compiler.semantic.SemanticException;
import lucis.compiler.semantic.Utility;

import java.util.*;

public record LucisKind(String module, String name, List<Parameter> parameters, StubType... bases) implements NamedObject {

    public LucisType apply(LucisType... types) {
        Objects.requireNonNull(types);
        int length = parameters.size();
        if (types.length != length)
            throw new SemanticException("kind " + this + " require " + length + " types to apply, but received " + types.length);
        Map<String, LucisType> parametersMap = new HashMap<>();
        List<KindType.Parameter> appliedParameters = new LinkedList<>();
        int i = 0;
        for (Parameter parameter : parameters) {
            Variant variant = parameter.variant();
            LucisType type = types[i];
            for (Constraint constraint : parameter.constraints())
                if (!constraint.satisfy(type)) throw new SemanticException(""); // TODO
            parametersMap.put(parameter.name, type);
            appliedParameters.add(new KindType.Parameter(variant, type));
            ++i;
        }
        LucisType[] bases = Arrays.stream(bases()).map(stub -> stub.apply(parametersMap)).toArray(LucisType[]::new);
        return new KindType(this, appliedParameters, bases);
    }

    @Override
    public String signature() {
        return module + Utility.LUCIS_DELIMITER_MODULE_SYMBOL + name
                + "<" + parameters.stream().map(Parameter::toString).reduce(String::concat).orElse("") + ">";
    }

    public record Constraint(Type type, LucisType reference) {
        public enum Type {
            SUPERTYPE,
            SUBTYPE,
        }

        public boolean satisfy(LucisType target) {
            return switch (type) {
                case SUPERTYPE -> reference.assignable(target);
                case SUBTYPE -> target.assignable(reference);
            };
        }

        @Override
        public String toString() {
            return switch (type) {
                case SUPERTYPE -> "<:" + reference;
                case SUBTYPE -> ":>" + reference;
            };
        }
    }

    public record Parameter(String name, Variant variant, Constraint... constraints) {
        @Override
        public String toString() {
            return name + variant;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LucisKind lucisKind)) return false;
        return Objects.equals(module, lucisKind.module) && Objects.equals(name, lucisKind.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(module, name);
    }
}
