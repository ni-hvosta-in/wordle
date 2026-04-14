package nihvostain.wordle_backend.game.services;

import nihvostain.wordle_backend.game.GameMode;
import nihvostain.wordle_backend.game.LetterStatus;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.game.NonExistentWordException;
import nihvostain.wordle_backend.game.modes.GameModeStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {

    WordService wordService;
    Map<GameMode, GameModeStrategy> strategies = new HashMap<>();
    public GameService(List<GameModeStrategy> strategies, WordService wordService) {
        this.wordService = wordService;

        for (GameModeStrategy strategy : strategies) {
            this.strategies.put(strategy.getGameMode(), strategy);
        }
    }

    public LetterStatus [] check (GameMode gameMode, Long id, String attempt, Level level) throws NonExistentWordException {

        if (!wordService.dictionaryContains(attempt)){
            throw new NonExistentWordException("Such a word does not exist");
        }

        return strategies.get(gameMode).check(id, attempt, level);
    }

}
