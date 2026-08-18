package nihvostain.wordle_backend.game.common.util;

import lombok.Getter;

@Getter
public enum LetterStatus {
    CORRECT,
    INCLUDES,
    UNUSED,
    WRONG
}
