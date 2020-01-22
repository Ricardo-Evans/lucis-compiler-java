package lucis.compiler.tokenizer;

import lucis.compiler.entity.Position;
import lucis.compiler.entity.Tag;
import lucis.compiler.entity.Token;
import lucis.compiler.io.Reader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultTokenizer implements Tokenizer {
    private Token nextToken = null;
    private Reader reader;
    private long lines = 1;
    private long lineOffset = 0;

    public DefaultTokenizer(Reader reader) {
        this.reader = reader;
    }

    @Override
    public Token next() {
        if (nextToken != null) {
            Token token = nextToken;
            nextToken = null;
            return token;
        }
        return tokenize();
    }

    @Override
    public Token peek() {
        if (nextToken == null) nextToken = tokenize();
        return nextToken;
    }

    // Get the next token
    private Token tokenize() {
        try {
            Character c = null;
            while (reader.available() && (c = reader.get()) != null) {
                if (!blankCharacters.contains(c)) {
                    reader.put(c);
                    break;
                }
                if (c == '\n') {
                    Token lineBreak = new Token(null, Tag.LINE_BREAK, new Position(lines, reader.position() - lineOffset + 1));
                    ++lines;
                    lineOffset = reader.position();
                    return lineBreak;
                }
            }
            if (c == null) return Token.END;
            Position position = new Position(lines, reader.position() - lineOffset + 1);
            if (digitCharacters.contains(c) || c == '-') return readNumber(position);
            if (notationCharacters.contains(c)) return readNotation(position);
            if (c == '"') return readString(position);
            if (c == '#') return readComment(position);
            return readWord(position);
        } catch (IOException e) {
            throw new TokenizeException(e);
        }
    }

    // Read a number, which may be an integer or decimal
    private Token readNumber(Position position) throws IOException {
        Character c = reader.get();
        Tag tag = Tag.INTEGER;
        boolean negative = false;
        if (c == '0') {
            c = reader.get();
            // TODO Recognize numbers such as 0xff and 0xFF and 0b11
            if (c == 'X') {
                // Read hexadecimal number
                long number = 0;
                while (reader.available() && (c = reader.get()) != null) {
                    if (digitCharacters.contains(c)) number = number * 16 + c - '0';
                    else if ('A' <= c && c <= 'F') number = number * 16 + 10 + c - 'A';
                    else {
                        reader.put(c);
                        break;
                    }
                }
                return new Token(number, tag, position);
            } else if (c == 'B') {
                // Read binary number
                long number = 0;
                while (reader.available() && (c = reader.get()) != null) {
                    if (c == '0' || c == '1') number = number * 2 + c - '0';
                    else {
                        reader.put(c);
                        break;
                    }
                }
                return new Token(number, tag, position);
            } else if (c == '.') tag = Tag.DECIMAL;
            else {
                // The number should be 0
                reader.put(c);
                return new Token(0L, tag, position);
            }
        } else if (c == '-') negative = true;
        else reader.put(c);
        long integer = 0;
        while (reader.available() && tag == Tag.INTEGER && (c = reader.get()) != null) {
            if (digitCharacters.contains(c)) integer = integer * 10 + c - '0';
            else {
                if (c == '.') tag = Tag.DECIMAL;
                else reader.put(c);
                break;
            }
        }
        double decimal = integer;
        double base = 1;
        while (reader.available() && tag == Tag.DECIMAL && (c = reader.get()) != null) {
            if (digitCharacters.contains(c)) decimal += (c - '0') / (base *= 10);
            else {
                reader.put(c);
                break;
            }
        }
        if (negative) {
            integer *= -1;
            decimal *= -1;
        }
        if (tag == Tag.INTEGER) return new Token(integer, tag, position);
        else return new Token(decimal, tag, position);
    }

    // Read a notation
    private Token readNotation(Position position) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (notationCharacters.contains(reader.peek()))
            builder.append(reader.get());
        String word = builder.toString();
        return new Token(word, notations.get(word), position);
    }

    // Read a string
    private Token readString(Position position) throws IOException {
        Character c = reader.get();
        assert c == '"';
        StringBuilder builder = new StringBuilder();
        while (reader.available() && (c = reader.get()) != null) {
            if (c == '"') return new Token(builder.toString(), Tag.STRING, position);
            if (c == '\\') {
                c = reader.get();
                if (escapes.containsKey(c)) reader.put(escapes.get(c));
                else throw new TokenizeException("cannot resolve escape symbol \\" + c);
            } else builder.append(c);
        }
        throw new TokenizeException("expected \" as the end of string");
    }

    // Read a block comment, contains a line begin with #
    private Token readComment(Position position) throws IOException {
        StringBuilder builder = new StringBuilder();
        Character c = reader.get();
        assert c == '#';
        while (reader.available() && (c = reader.get()) != null) {
            if (c == '\n') {
                reader.put(c);
                break;
            }
            builder.append(c);
        }
        return new Token(builder.toString(), Tag.COMMENT, position);
    }

    // Read a word, which may be an identifier or keyword
    private Token readWord(Position position) throws IOException {
        StringBuilder builder = new StringBuilder();
        Character c;
        while (reader.available() && (c = reader.get()) != null) {
            if (blankCharacters.contains(c) || notationCharacters.contains(c)) {
                reader.put(c);
                break;
            }
            builder.append(c);
        }
        String word = builder.toString();
        Tag tag = keywords.getOrDefault(word, Tag.IDENTIFIER);
        return new Token(word, tag, position);
    }

    private static final Set<Character> blankCharacters = Set.of(' ', '\t', '\r', '\n');
    private static final Set<Character> digitCharacters = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static final Set<Character> notationCharacters = Set.of(',', '.', '+', '-', '*', '/', '%', '(', ')', '[', ']', '{', '}', '<', '>', '=', '!', '&', '|', '^', ':', ';', '?');
    private static final Map<String, Tag> keywords = new HashMap<>();
    private static final Map<String, Tag> notations = new HashMap<>();
    private static final Map<Character, Character> escapes = new HashMap<>();

    // Initialize static data
    static {
        // Construct the map of escapes characters
        escapes.put('r', '\r');
        escapes.put('n', '\n');
        escapes.put('t', '\t');
        escapes.put('\\', '\\');
        escapes.put('"', '"');
        // Construct the map of keywords
        keywords.put("if", Tag.IF);
        keywords.put("else", Tag.ELSE);
        keywords.put("while", Tag.WHILE);
        // Construct the map of notations
        notations.put("=", Tag.ASSIGN);
        notations.put("<", Tag.LESS);
        notations.put(">", Tag.GREATER);
        notations.put("==", Tag.EQUAL);
        notations.put("<=", Tag.LESS_EQUAL);
        notations.put(">=", Tag.GREATER_EQUAL);
        notations.put(".", Tag.DOT);
        notations.put("?", Tag.QUESTION);
    }
}
