package nihvostain.wordle_backend.auth.controller;
import jakarta.validation.Valid;
import nihvostain.wordle_backend.auth.services.AuthService;
import nihvostain.wordle_backend.auth.dto.AuthRequest;
import nihvostain.wordle_backend.auth.dto.AuthResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.register(authRequest));
    }
}
