package nihvostain.wordle_backend.game.personal.dto;

import nihvostain.wordle_backend.game.personal.model.Attempt;

import java.util.List;

public record UserAttempts (
        List<Attempt> attempts
) {}
