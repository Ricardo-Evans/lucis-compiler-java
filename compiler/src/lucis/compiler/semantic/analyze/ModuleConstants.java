package lucis.compiler.semantic.analyze;

public final class ModuleConstants {
    public static final int MAGIC = 0xB09AFC73;
    public static final byte EXTEND_MASK = (byte) 0X80;
    public static final byte NUMBER_MASK = (byte) 0X7F;

    public static final byte HIGHER_MASK = (byte) 0xF0;
    public static final byte LOWER_MASK = (byte) 0x0F;
    public static final byte INTEGER_FLAG = (byte) 0X10;
    public static final byte DECIMAL_FLAG = (byte) 0X20;
    public static final byte STRING_FLAG = (byte) 0X30;
    public static final byte FUNCTION_FLAG = (byte) 0X40;
    public static final byte TYPE_FLAG = (byte) 0X50;
    public static final byte KIND_FLAG = (byte) 0X60;
    public static final byte SIGNATURE_FLAG = (byte) 0X70;
    public static final byte TUPLE_FLAG = (byte) 0X80;
    public static final byte KIND_RESULT_FLAG = (byte) 0X0F;
    @SuppressWarnings("PointlessBitwiseExpression")
    public static final byte FUNCTION_DYNAMIC_MASK = (byte) 1 << 0;
    public static final byte FUNCTION_OVERRIDE_MASK = (byte) 1 << 1;
    public static final byte FUNCTION_NATIVE_MASK = (byte) 1 << 2;
    public static final byte FUNCTION_ABSTRACT_MASK = (byte) 1 << 3;

    private ModuleConstants() {
    }
}
