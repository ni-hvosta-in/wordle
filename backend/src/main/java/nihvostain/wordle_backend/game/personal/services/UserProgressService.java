package nihvostain.wordle_backend.game.personal.services;

import jakarta.persistence.EntityNotFoundException;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.dto.ProfileInfo;
import nihvostain.wordle_backend.game.personal.entity.UserGame;
import nihvostain.wordle_backend.game.personal.entity.UserGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProgressService {

    private final UserGameRepository userGameRepository;
    private final int base_prize = 10;
    public UserProgressService(UserGameRepository userGameRepository) {
        this.userGameRepository = userGameRepository;

    }

    public int getUserIndex(Long userId, Level level) {
        UserGame userGame = getUserGame(userId);
        return userGame.getIndexByLevel(level);
    }

    @Transactional
    public void incrementUserIndex(Long userId, Level level) {
        UserGame userGame = getUserGame(userId);
        int index = userGame.getIndexByLevel(level);
        userGame.setIndexByLevel(level, index + 1);
        userGameRepository.save(userGame);
    }

    private UserGame getUserGame(Long id) {
        return userGameRepository.findByUserId(id).orElseThrow(
                () -> new EntityNotFoundException("User game not found")
        );
    }

    @Transactional
    public void correctWord(Long userId){
        UserGame userGame = getUserGame(userId);
        userGame.setCoins(userGame.getCoins() + base_prize);
        userGameRepository.save(userGame);
    }

    public ProfileInfo getUserProfileInfo(Long userId, Level level) {

        UserGame userGame = getUserGame(userId);
        return new ProfileInfo(userGame.getIndexByLevel(level), userGame.getCoins());
    }
}
