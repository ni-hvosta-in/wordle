package nihvostain.wordle_backend.game;

public class UserIndexOutOfBoundsException extends RuntimeException {
    public UserIndexOutOfBoundsException(String message) {
        super(message);
    }
}
