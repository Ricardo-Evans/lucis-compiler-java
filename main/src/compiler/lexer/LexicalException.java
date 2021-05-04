package compiler.lexer;

public class LexicalException extends RuntimeException {
    LexicalException() {
    }

    LexicalException(String message) {
        super(message);
    }

    LexicalException(String message, Throwable cause) {
        super(message, cause);
    }

    LexicalException(Throwable cause) {
        super(cause);
    }
}
