package nihvostain.wordle_backend.game.dto;

import nihvostain.wordle_backend.game.LetterStatus;

public record GameResponse(
        LetterStatus[] statuses
) {}
