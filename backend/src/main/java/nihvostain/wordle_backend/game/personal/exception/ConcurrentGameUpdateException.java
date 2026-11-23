package nihvostain.wordle_backend.game.personal.exception;

public class ConcurrentGameUpdateException extends RuntimeException {
    public ConcurrentGameUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
