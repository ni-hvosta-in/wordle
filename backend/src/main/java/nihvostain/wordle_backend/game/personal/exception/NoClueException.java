package nihvostain.wordle_backend.game.personal.exception;

public class NoClueException extends RuntimeException {
    public NoClueException(String message) {
        super(message);
    }
}
