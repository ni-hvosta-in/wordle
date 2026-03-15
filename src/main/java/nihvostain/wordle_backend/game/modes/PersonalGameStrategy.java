package nihvostain.wordle_backend.game.modes;

import nihvostain.wordle_backend.game.GameMode;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.game.entity.UserGame;
import nihvostain.wordle_backend.game.entity.UserGameRepository;
import nihvostain.wordle_backend.game.services.WordChecker;
import nihvostain.wordle_backend.game.services.WordService;

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
    public String[] check(Long id, String attempt, Level level) {

        UserGame userGame = userGameRepository.findByUserId(id).orElseThrow(
                () -> new RuntimeException("User game not found")
        );

        String target = wordService.getPersonalWord(level, userGame.getIndexByLevel(level));
        return wordChecker.checkWord(attempt, target);
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.PERSONAL;
    }
}
