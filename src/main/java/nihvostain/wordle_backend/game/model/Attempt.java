package nihvostain.wordle_backend.game.model;

import nihvostain.wordle_backend.game.LetterStatus;

public record Attempt (
    String word,
    LetterStatus [] statuses
) {}
