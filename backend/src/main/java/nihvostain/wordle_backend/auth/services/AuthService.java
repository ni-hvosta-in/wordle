package nihvostain.wordle_backend.auth.services;

import nihvostain.wordle_backend.auth.dto.AuthRequest;
import nihvostain.wordle_backend.auth.dto.AuthResponse;
import nihvostain.wordle_backend.auth.exceptions.AuthException;
import nihvostain.wordle_backend.common.services.JWTService;
import nihvostain.wordle_backend.auth.user.User;
import nihvostain.wordle_backend.auth.user.UserRepository;
import nihvostain.wordle_backend.game.personal.entity.UserProfile;
import nihvostain.wordle_backend.game.personal.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final UserProfileRepository userProfileRepository;

    public AuthService(UserRepository userRepository, JWTService jwtService, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userProfileRepository = userProfileRepository;
    }

    public AuthResponse login(AuthRequest authRequest) throws AuthException {

        User user = userRepository.findUserByLogin(authRequest.login()).orElseThrow(() ->
                new AuthException("Invalid login")
        );

        if (!user.getPassword().equals(authRequest.password())){
            throw new AuthException("Invalid password");
        }

        if (!userProfileRepository.existsByUser(user)){
            userProfileRepository.save(new UserProfile(user, 0));
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
        userProfileRepository.save(new UserProfile(user, 0));

        return new AuthResponse("auth", jwtService.generateToken(user));

    }
}
