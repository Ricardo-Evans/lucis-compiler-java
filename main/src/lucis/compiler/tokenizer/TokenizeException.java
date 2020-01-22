package lucis.compiler.tokenizer;

public class TokenizeException extends RuntimeException {
    TokenizeException() {
    }

    TokenizeException(String message) {
        super(message);
    }

    TokenizeException(String message, Throwable cause) {
        super(message, cause);
    }

    TokenizeException(Throwable cause) {
        super(cause);
    }
}
