package nihvostain.wordle_backend.auth.exceptions;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
