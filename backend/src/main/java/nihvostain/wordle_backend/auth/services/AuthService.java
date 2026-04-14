package nihvostain.wordle_backend.auth.services;

import nihvostain.wordle_backend.auth.dto.AuthRequest;
import nihvostain.wordle_backend.auth.dto.AuthResponse;
import nihvostain.wordle_backend.auth.exceptions.AuthException;
import nihvostain.wordle_backend.common.services.JWTService;
import nihvostain.wordle_backend.game.entity.UserGame;
import nihvostain.wordle_backend.game.entity.UserGameRepository;
import nihvostain.wordle_backend.user.User;
import nihvostain.wordle_backend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    UserRepository userRepository;
    JWTService jwtService;
    UserGameRepository userGameRepository;

    public AuthService(UserRepository userRepository, JWTService jwtService, UserGameRepository userGameRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userGameRepository = userGameRepository;
    }

    public AuthResponse login(AuthRequest authRequest) throws AuthException {

        User user = userRepository.findUserByLogin(authRequest.login()).orElseThrow(() ->
                new AuthException("Invalid login")
        );

        if (!user.getPassword().equals(authRequest.password())){
            throw new AuthException("Invalid password");
        }

        if (!userGameRepository.existsByUser(user)){
            userGameRepository.save(new UserGame(user));
        }

        return new AuthResponse("auth", jwtService.generateToken(user));

    }

    @Transactional
    public AuthResponse register(AuthRequest authRequest) throws AuthException {

        if (userRepository.existsUserByLogin(authRequest.login())) {
            throw new AuthException("User already exists");
        }

        User user = new User(authRequest.login(), authRequest.password());
        userRepository.save(user);
        userGameRepository.save(new UserGame(user));

        return new AuthResponse("auth", jwtService.generateToken(user));

    }
}
