package lucis.compiler.semantic.analyze;

import compiler.entity.BinaryData;
import compiler.semantic.SemanticException;
import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

public final class ModuleParser {
    private static final int MAGIC = 0xB09AFC73;
    private static final byte EXTEND_MASK = (byte) 0X80;
    private static final byte NUMBER_MASK = (byte) 0X7F;
    private static final byte INTEGER_FLAG = (byte) 0X01;
    private static final byte DECIMAL_FLAG = (byte) 0X02;
    private static final byte STRING_FLAG = (byte) 0X04;
    private static final byte FUNCTION_FLAG = (byte) 0X08;
    private static final byte TYPE_FLAG = (byte) 0X10;
    private static final byte KIND_FLAG = (byte) 0X20;
    private static final byte SIGNATURE_FLAG = (byte) 0X40;
    private static final byte TUPLE_FLAG = (byte) 0X80;

    private final BinaryData data;
    private final Environment environment;
    private String moduleName;
    private int version;
    private List<LucisObject> constantPool;
    private Map<String, Set<Symbol>> symbolTable;

    private ModuleParser(BinaryData data, Environment environment) {
        this.data = data;
        this.environment = environment;
    }

    public static Module parseModule(Environment environment, BinaryData data) {
        return new ModuleParser(data, environment).parseModule();
    }

    private Module parseModule() {
        int magic = parseMagic();
        if (magic != MAGIC) throw new SemanticException();
        int nameLength = parseExtendableNumber();
        moduleName = data.readString(nameLength, StandardCharsets.UTF_8);
        version = parseExtendableNumber();
        parseDependencies();
        parseConstantPool();
        parseSymbolTable();
        return new Module(moduleName, constantPool, symbolTable);
    }

    private int parseMagic() {
        return data.readInteger32();
    }

    private int parseExtendableNumber() {
        int result = 0;
        byte b;
        do {
            b = data.read();
            result <<= 7;
            result |= b & NUMBER_MASK;
        } while ((b & EXTEND_MASK) != 0);
        return result;
    }

    private void parseDependencies() {
        int count = parseExtendableNumber();
        for (int i = 0; i < count; ++i) {
            String name = parseString().toString();
            environment.loadModule(name).orElseThrow(Utility.moduleNotFound(name));
        }
    }

    private void parseConstantPool() {
        int constantCount = parseExtendableNumber();
        constantPool = new ArrayList<>(constantCount);
        for (int i = 0; i < constantCount; ++i) constantPool.set(i, parseConstant());
    }

    private LucisObject parseConstant() {
        byte flag = data.read();
        return switch (flag) {
            case INTEGER_FLAG -> parseInteger();
            case DECIMAL_FLAG -> parseDecimal();
            case STRING_FLAG -> parseString();
            case SIGNATURE_FLAG -> parseSignature();
            case TUPLE_FLAG -> parseTuple();
            default -> throw new SemanticException("unsupported constant flag: " + flag);
        };
    }

    private LucisInteger parseInteger() {
        int size = parseExtendableNumber();
        long value = switch (size) {
            case 4 -> data.readInteger32();
            case 8 -> data.readInteger64();
            default -> throw new SemanticException("unsupported integer size of " + size + " bytes");
        };
        return new LucisInteger(value);
    }

    private LucisDecimal parseDecimal() {
        int size = parseExtendableNumber();
        double value = switch (size) {
            case 4 -> data.readDecimal32();
            case 8 -> data.readDecimal64();
            default -> throw new SemanticException("unsupported decimal size of " + size + " bytes");
        };
        return new LucisDecimal(value);
    }

    private LucisString parseString() {
        int size = parseExtendableNumber();
        byte[] binaryString = data.readNBytes(size);
        return new LucisString(binaryString);
    }

    private LucisObject parseSignature() {
        int index = parseExtendableNumber();
        LucisString signature = (LucisString) constantPool.get(index);
        return environment.requireObject(signature.toString());
    }

    private LucisTuple parseTuple() {
        int size = parseExtendableNumber();
        LucisObject[] contents = new LucisObject[size];
        for (int i = 0; i < size; ++i) {
            int index = parseExtendableNumber();
            contents[i] = constantPool.get(index);
        }
        return new LucisTuple(contents);
    }

    private void parseSymbolTable() {
        int symbolCount = parseExtendableNumber();
        symbolTable = new HashMap<>();
        for (int i = 0; i < symbolCount; ++i) {
            Symbol symbol = parseSymbol();
            symbolTable.putIfAbsent(symbol.name(), new HashSet<>());
            if (!symbolTable.get(symbol.name()).add(symbol)) throw Utility.symbolAlreadyExist(symbol).get();
        }
    }

    private Symbol parseSymbol() {
        int nameIndex = parseExtendableNumber();
        int typeIndex = parseExtendableNumber();
        int valueIndex = parseExtendableNumber();
        String name = constantPool.get(nameIndex).toString();
        LucisType type = (LucisType) constantPool.get(typeIndex);
        LucisObject value = constantPool.get(valueIndex);
        return new Symbol(name, moduleName, type, value);
    }
}
