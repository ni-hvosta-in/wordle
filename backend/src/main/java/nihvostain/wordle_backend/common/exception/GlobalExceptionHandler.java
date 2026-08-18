package nihvostain.wordle_backend.common.exception;

import nihvostain.wordle_backend.auth.exceptions.AuthException;
import nihvostain.wordle_backend.game.common.exception.NonExistentWordException;
import nihvostain.wordle_backend.game.personal.exception.NoClueException;
import nihvostain.wordle_backend.game.personal.exception.UserIndexOutOfBoundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NonExistentWordException.class)
    public ResponseEntity<ErrorResponse> handleNonExistentWordException(NonExistentWordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserIndexOutOfBoundsException.class)
    public ResponseEntity<ErrorResponse> handleUserIndexOutOfBoundsException(UserIndexOutOfBoundsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NoClueException.class)
    public ResponseEntity<ErrorResponse> handleNoClueException(NoClueException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }
}
