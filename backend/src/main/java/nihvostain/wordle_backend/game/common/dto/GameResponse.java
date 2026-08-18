package nihvostain.wordle_backend.game.common.dto;

import nihvostain.wordle_backend.game.common.util.LetterStatus;

public record GameResponse(
        LetterStatus[] statuses
) {}
