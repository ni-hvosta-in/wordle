package nihvostain.wordle_backend.game.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nihvostain.wordle_backend.game.Level;

public record GameRequest(
        @NotBlank
        String attempt,

        @NotNull
        Level level
) {}
