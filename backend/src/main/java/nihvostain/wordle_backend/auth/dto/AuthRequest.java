package nihvostain.wordle_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank
        String login,

        @NotBlank
        String password
) {}
