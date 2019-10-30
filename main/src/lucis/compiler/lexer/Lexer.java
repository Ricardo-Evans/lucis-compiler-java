package lucis.compiler.lexer;

import lucis.compiler.entity.Document;
import lucis.compiler.entity.Position;
import lucis.compiler.entity.Token;
import lucis.compiler.entity.Tag;
import lucis.compiler.io.Reader;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Lexer {
    private static final Set<Character> blankCharacters = Set.of(' ', '\t', '\r', '\n');
    private static final Set<Character> digitCharacters = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static final Set<Character> notationCharacters = Set.of(',', '.', '+', '-', '*', '/', '%', '(', ')', '[', ']', '{', '}', '<', '>', '=', '!', '&', '|', '^', ':', ';', '?');
    private static final Map<String, Tag> keywords = new HashMap<>();
    private static final Map<String, Tag> notations = new HashMap<>();
    private static final Map<Character, Character> escapes = new HashMap<>();

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
    }

    private static class PositionMap {
        private List<Long> lines = new ArrayList<>(); // Store the begin positions of each line in order.

        Position get(long position) {
            int line = find(position);
            return new Position(line, position - lines.get(line - 1) + 1);
        }

        void set(long position) {
            int index = find(position);
            lines.add(index, position);
        }

        private int find(long position) {
            return find(position, 0, lines.size());
        }

        private int find(long position, int start, int end) {
            int medium = (start + end) / 2;
            if (end - start <= 1) return end;
            long current = lines.get(medium);
            if (current < position) return find(position, medium, end);
            if (current > position) return find(position, start, medium);
            return medium + 1;
        }
    }

    public Lexer() {
    }

    // Read a number, which may be an integer or decimal
    private Token readNumber(Reader reader, Position position) throws IOException {
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
    private Token readNotation(Reader reader, Position position) throws IOException {
        StringBuilder builder = new StringBuilder();
        Character c = reader.get();
        assert notationCharacters.contains(c);
        builder.append(c);
        if (c == '>' || c == '<' || c == '=' || c == '!') {
            c = reader.get();
            if (c == '=') builder.append(c);
            else reader.put(c);
        }
        String word = builder.toString();
        return new Token(word, notations.get(word), position);
    }

    // Read a string
    private Token readString(Reader reader, Position position) throws IOException {
        Character c = reader.get();
        assert c == '"';
        StringBuilder builder = new StringBuilder();
        while (reader.available() && (c = reader.get()) != null) {
            if (c == '"') return new Token(builder.toString(), Tag.STRING, position);
            if (c == '\\') {
                c = reader.get();
                if (escapes.containsKey(c)) reader.put(escapes.get(c));
                else throw new LexicalException("cannot resolve escape symbol \\" + c);
            } else builder.append(c);
        }
        throw new LexicalException("expected \" as the end of string");
    }

    // Read a block comment, contains a line begin with #
    private Token readComment(Reader reader, Position position) throws IOException {
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
    private Token readWord(Reader reader, Position position) throws IOException {
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

    // Get the next token
    private Token readToken(Reader reader, PositionMap positionMap) throws IOException {
        Character c = null;
        while (reader.available() && (c = reader.get()) != null) {
            if (!blankCharacters.contains(c)) {
                reader.put(c);
                break;
            }
            if (c == '\n') positionMap.set(reader.position());
        }
        if (c == null) return Token.END;
        Position position = positionMap.get(reader.position());
        if (digitCharacters.contains(c) || c == '-') return readNumber(reader, position);
        if (notationCharacters.contains(c)) return readNotation(reader, position);
        if (c == '"') return readString(reader, position);
        if (c == '#') return readComment(reader, position);
        return readWord(reader, position);
    }

    /**
     * Tokenize the given source
     *
     * @return Return the tokenized document
     */
    public Document tokenize(Reader reader) {
        PositionMap positionMap = new PositionMap();
        List<Token> tokens = new LinkedList<>();
        positionMap.set(0);
        Token token;
        try {
            while ((token = readToken(reader, positionMap)) != Token.END)
                tokens.add(token);
        } catch (IOException e) {
            System.err.println("tokenize failed because of io error: " + e);
            e.printStackTrace();
        } catch (LexicalException e) {
            System.err.println("tokenize failed because " + e + " at " + positionMap.get(reader.position()));
            e.printStackTrace();
        }
        return new Document(tokens);
    }

    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        Reader reader = new Reader(FileChannel.open(new File("test-source.lux").toPath(), StandardOpenOption.READ));
        Document document = lexer.tokenize(reader);
        System.out.println("done");
    }

}
