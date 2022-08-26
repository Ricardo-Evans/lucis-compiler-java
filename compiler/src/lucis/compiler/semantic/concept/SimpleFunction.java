package lucis.compiler.semantic.concept;

import lucis.compiler.semantic.Utility;

public class SimpleFunction implements LucisFunction {
    private final String module;
    private final String name;
    private final String signature;
    private final boolean isDynamic;
    private final boolean isOverride;
    private final boolean isNative;
    private final boolean isAbstract;
    private final LucisType parameterType;
    private final LucisType resultType;
    private final LucisFunction overrideFunction;
    private final int stackSize;

    public SimpleFunction(String module, String name, boolean isDynamic, boolean isOverride, boolean isNative, boolean isAbstract, LucisType parameterType, LucisType resultType, LucisFunction overrideFunction, int stackSize) {
        this.module = module;
        this.name = name;
        this.signature = Utility.signature(module, name);
        this.isDynamic = isDynamic;
        this.isOverride = isOverride;
        this.isNative = isNative;
        this.isAbstract = isAbstract;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.overrideFunction = overrideFunction;
        this.stackSize = stackSize;
    }

    @Override
    public LucisType parameterType() {
        return parameterType;
    }

    @Override
    public LucisType resultType() {
        return resultType;
    }

    @Override
    public int stackSize() {
        return stackSize;
    }

    @Override
    public boolean isDynamic() {
        return isDynamic;
    }

    @Override
    public boolean isOverride() {
        return isOverride;
    }

    @Override
    public boolean isNative() {
        return isNative;
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String module() {
        return module;
    }

    @Override
    public String signature() {
        return signature;
    }
}
