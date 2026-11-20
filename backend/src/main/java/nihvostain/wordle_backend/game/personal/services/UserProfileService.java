package nihvostain.wordle_backend.game.personal.services;

import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.dto.ProfileInfo;
import nihvostain.wordle_backend.game.personal.entity.UserProfile;
import nihvostain.wordle_backend.game.personal.exception.UserProfileNotFoundException;
import nihvostain.wordle_backend.game.personal.repository.GameRepository;
import nihvostain.wordle_backend.game.personal.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    private static final int BASE_REWARD = 10;
    private final UserProfileRepository userProfileRepository;
    private final GameService gameService;

    public UserProfileService(UserProfileRepository userProfileRepository, GameRepository gameRepository, GameService gameService) {
        this.userProfileRepository = userProfileRepository;
        this.gameService = gameService;
    }


    private UserProfile getUserProfile(Long id) {
        return userProfileRepository.findByUserId(id).orElseThrow(
                () -> new UserProfileNotFoundException("User profile not found")
        );
    }

    @Transactional
    public void correctWord(Long userId){
        UserProfile profile = getUserProfile(userId);
        profile.setCoins(profile.getCoins() + BASE_REWARD);
        userProfileRepository.save(profile);
    }

    public ProfileInfo getUserProfileInfo(Long userId, Level level) {
        UserProfile profile = getUserProfile(userId);
        return new ProfileInfo(gameService.getLevelNumberByUserIdAndLevel(userId, level), profile.getCoins());
    }
}
