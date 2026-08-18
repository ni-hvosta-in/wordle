package nihvostain.wordle_backend.game.common.exception;

public class NonExistentWordException extends RuntimeException {
    public NonExistentWordException(String message) {
        super(message);
    }
}
