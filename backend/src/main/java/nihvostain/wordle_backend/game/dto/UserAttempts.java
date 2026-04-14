package nihvostain.wordle_backend.game.dto;

import nihvostain.wordle_backend.game.model.Attempt;

import java.util.List;

public record UserAttempts (
        List<Attempt> attempts
) {}
