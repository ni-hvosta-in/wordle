package nihvostain.wordle_backend.common.exception;

import nihvostain.wordle_backend.auth.exceptions.AuthException;
import nihvostain.wordle_backend.game.common.exception.NonExistentWordException;
import nihvostain.wordle_backend.game.personal.exception.ConcurrentGameUpdateException;
import nihvostain.wordle_backend.game.personal.exception.FinishedGameNotFoundException;
import nihvostain.wordle_backend.game.personal.exception.GameInProgressException;
import nihvostain.wordle_backend.game.personal.exception.NoClueException;
import nihvostain.wordle_backend.game.personal.exception.UserIndexOutOfBoundsException;
import nihvostain.wordle_backend.game.personal.exception.UserProfileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
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

    @ExceptionHandler({FinishedGameNotFoundException.class, UserProfileNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(GameInProgressException.class)
    public ResponseEntity<ErrorResponse> handleGameInProgressException(GameInProgressException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({OptimisticLockingFailureException.class, ConcurrentGameUpdateException.class})
    public ResponseEntity<ErrorResponse> handleConcurrentModification(RuntimeException ex) {
        log.warn("Concurrent modification rejected: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Request conflicts with another one in progress, please retry"));
    }
}
