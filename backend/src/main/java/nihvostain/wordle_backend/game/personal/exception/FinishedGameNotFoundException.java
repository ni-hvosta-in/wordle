package nihvostain.wordle_backend.game.personal.exception;

public class FinishedGameNotFoundException extends RuntimeException {
    public FinishedGameNotFoundException(String message) {
        super(message);
    }
}
