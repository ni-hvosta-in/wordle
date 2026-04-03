package nihvostain.wordle_backend.game.modes;

import nihvostain.wordle_backend.game.GameMode;
import nihvostain.wordle_backend.game.LetterStatus;
import nihvostain.wordle_backend.game.Level;

public interface GameModeStrategy {
    LetterStatus[] check(Long id, String attempt, Level level);
    GameMode getGameMode();
}
