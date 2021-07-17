package compiler.parser;

public class GrammaticalException extends RuntimeException {
    public GrammaticalException() {
    }

    public GrammaticalException(String message) {
        super(message);
    }

    public GrammaticalException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrammaticalException(Throwable cause) {
        super(cause);
    }

    public GrammaticalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
