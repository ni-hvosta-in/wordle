package nihvostain.wordle_backend.game.model;

import nihvostain.wordle_backend.game.LetterStatus;

import java.util.Arrays;

public record Attempt (
    String word,
    LetterStatus [] statuses
) {
    @Override
    public String toString() {
        return "word %s : %s".formatted(word, Arrays.toString(statuses));
    }
}
