package compiler.semantic;

public class SemanticException extends RuntimeException{
    public SemanticException() {
    }

    public SemanticException(String message) {
        super(message);
    }

    public SemanticException(String message, Throwable cause) {
        super(message, cause);
    }

    public SemanticException(Throwable cause) {
        super(cause);
    }

    public SemanticException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
