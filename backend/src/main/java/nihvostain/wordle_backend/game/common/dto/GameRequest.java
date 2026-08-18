package nihvostain.wordle_backend.game.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nihvostain.wordle_backend.game.common.util.Level;

public record GameRequest(
        @NotBlank
        String attempt,

        @NotNull
        Level level
) {}
