package nihvostain.wordle_backend.game.modes;

import nihvostain.wordle_backend.game.GameMode;
import nihvostain.wordle_backend.game.LetterStatus;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.game.entity.UserGame;
import nihvostain.wordle_backend.game.entity.UserGameRepository;
import nihvostain.wordle_backend.game.services.WordChecker;
import nihvostain.wordle_backend.game.services.WordService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class PersonalGameStrategy implements GameModeStrategy {

    UserGameRepository userGameRepository;
    WordChecker wordChecker;
    WordService wordService;

    public PersonalGameStrategy(UserGameRepository userGameRepository, WordChecker wordChecker, WordService wordService) {
        this.userGameRepository = userGameRepository;
        this.wordChecker = wordChecker;
        this.wordService = wordService;
    }
    @Override
    @Transactional
    public LetterStatus[] check(Long id, String attempt, Level level) {

        UserGame userGame = userGameRepository.findByUserId(id).orElseThrow(
                () -> new RuntimeException("User game not found")
        );

        int index = userGame.getIndexByLevel(level);
        String target = wordService.getPersonalWord(level, index);
        LetterStatus [] statuses = wordChecker.checkWord(target, attempt);
        if (Arrays.stream(statuses).allMatch((status) -> status.equals(LetterStatus.CORRECT))) {
            userGame.setIndexByLevel(level, index + 1);
        }
        return wordChecker.checkWord(attempt, target);
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.PERSONAL;
    }
}
