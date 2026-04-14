package nihvostain.wordle_backend.game.services;

import jakarta.persistence.EntityNotFoundException;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.game.entity.UserGame;
import nihvostain.wordle_backend.game.entity.UserGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProgressService {

    private final UserGameRepository userGameRepository;

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
}
