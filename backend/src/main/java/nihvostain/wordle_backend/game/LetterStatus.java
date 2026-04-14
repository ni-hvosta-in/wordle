package nihvostain.wordle_backend.game;

import lombok.Getter;

@Getter
public enum LetterStatus {
    CORRECT,
    INCLUDES,
    UNUSED,
    WRONG
}
