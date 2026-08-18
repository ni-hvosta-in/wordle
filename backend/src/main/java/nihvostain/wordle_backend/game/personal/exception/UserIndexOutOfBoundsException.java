package nihvostain.wordle_backend.game.personal.exception;

public class UserIndexOutOfBoundsException extends RuntimeException {
    public UserIndexOutOfBoundsException(String message) {
        super(message);
    }
}
