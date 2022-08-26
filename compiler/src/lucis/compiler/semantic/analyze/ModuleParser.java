package lucis.compiler.semantic.analyze;

import compiler.entity.BinaryData;
import compiler.semantic.SemanticException;
import lucis.compiler.semantic.Utility;
import lucis.compiler.semantic.concept.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

public final class ModuleParser {
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
        if (magic != ModuleConstants.MAGIC) throw new SemanticException();
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
            result |= b & ModuleConstants.NUMBER_MASK;
        } while ((b & ModuleConstants.EXTEND_MASK) != 0);
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
        byte higher = (byte) (flag & ModuleConstants.HIGHER_MASK);
        byte lower = (byte) (flag & ModuleConstants.LOWER_MASK);
        return switch (higher) {
            case ModuleConstants.INTEGER_FLAG -> parseInteger(lower);
            case ModuleConstants.DECIMAL_FLAG -> parseDecimal(lower);
            case ModuleConstants.STRING_FLAG -> parseString();
            case ModuleConstants.FUNCTION_FLAG -> parseFunction(lower);
            case ModuleConstants.TYPE_FLAG -> parseType(lower);
            case ModuleConstants.KIND_FLAG -> parseKind(lower);
            case ModuleConstants.SIGNATURE_FLAG -> parseSignature();
            case ModuleConstants.TUPLE_FLAG -> parseTuple();
            default -> throw new SemanticException("unsupported constant flag: " + flag);
        };
    }

    private LucisInteger parseInteger(byte flag) {
        if (flag == 0) return new LucisInteger(parseExtendableNumber());
        int size = 1 << (flag - 1);
        long value = switch (size) {
            case 4 -> data.readInteger32();
            case 8 -> data.readInteger64();
            default -> throw new SemanticException("unsupported integer size of " + size + " bytes");
        };
        return new LucisInteger(value);
    }

    private LucisDecimal parseDecimal(byte flag) {
        if (flag == 0) return new LucisDecimal(Float.intBitsToFloat(parseExtendableNumber()));
        int size = 1 << (flag - 1);
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

    private LucisFunction parseFunction(byte flag) {
        if (flag == ModuleConstants.KIND_RESULT_FLAG) {
        }
        boolean isDynamic = (flag & ModuleConstants.FUNCTION_DYNAMIC_MASK) != 0;
        boolean isOverride = (flag & ModuleConstants.FUNCTION_OVERRIDE_MASK) != 0;
        boolean isNative = (flag & ModuleConstants.FUNCTION_NATIVE_MASK) != 0;
        boolean isAbstract = (flag & ModuleConstants.FUNCTION_ABSTRACT_MASK) != 0;
        String functionName = parseString().toString();
        LucisTuple parameterTypes = (LucisTuple) constantPool.get(parseExtendableNumber());
        LucisTuple resultTypes = (LucisTuple) constantPool.get(parseExtendableNumber());
        LucisFunction overrideFunction = isOverride ? (LucisFunction) constantPool.get(parseExtendableNumber()) : null;
        LucisTuple dynamicParameters = isDynamic ? (LucisTuple) constantPool.get(parseExtendableNumber()) : null;
        int stackSize = isNative ? -1 : parseExtendableNumber();
        int functionContentSize = isNative ? -1 : parseExtendableNumber();
        // TODO: parse bytecodes
        return null;
    }

    private LucisType parseType(byte flag) {
        return null;
    }

    private LucisKind parseKind(byte flag) {
        return null;
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
