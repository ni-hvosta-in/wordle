package nihvostain.wordle_backend.game.modes;

import nihvostain.wordle_backend.game.GameMode;
import nihvostain.wordle_backend.game.LetterStatus;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.game.services.WordChecker;
import nihvostain.wordle_backend.game.services.WordService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class DailyGameStrategy implements GameModeStrategy {

    WordService wordService;
    WordChecker wordChecker;

    public DailyGameStrategy(WordService wordService, WordChecker wordChecker) {
        this.wordService = wordService;
        this.wordChecker = wordChecker;
    }

    @Override
    public LetterStatus[] check(Long id, String attempt, Level level) {
        String target = wordService.getDailyWord(level);
        return wordChecker.checkWord(attempt, target);
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.DAILY;
    }
}
