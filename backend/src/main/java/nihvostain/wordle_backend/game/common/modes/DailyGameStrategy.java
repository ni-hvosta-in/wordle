package nihvostain.wordle_backend.game.common.modes;

import nihvostain.wordle_backend.game.common.util.GameMode;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.common.services.WordChecker;
import nihvostain.wordle_backend.game.common.services.WordService;
import org.springframework.stereotype.Component;

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
