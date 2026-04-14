package nihvostain.wordle_backend.game;

public class NonExistentWordException extends RuntimeException {
    public NonExistentWordException(String message) {
        super(message);
    }
}
