package lucis.compiler.semantic;

import lucis.compiler.ir.LucisDecimal;
import lucis.compiler.ir.LucisInteger;
import lucis.compiler.ir.LucisString;

import java.nio.charset.StandardCharsets;

public final class Utility {
    private Utility() {
    }

    public static LucisInteger integer(String s) {
        return null;
    }

    public static LucisDecimal decimal(String s) {
        return null;
    }

    public static LucisString string(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        return new LucisString(bytes);
    }
}
