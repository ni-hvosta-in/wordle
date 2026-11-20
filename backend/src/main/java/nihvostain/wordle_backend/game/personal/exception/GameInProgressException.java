package nihvostain.wordle_backend.game.personal.exception;

public class GameInProgressException extends RuntimeException {
    public GameInProgressException(String message) {
        super(message);
    }
}
