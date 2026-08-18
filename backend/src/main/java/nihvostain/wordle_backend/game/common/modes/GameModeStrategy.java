package nihvostain.wordle_backend.game.common.modes;

import nihvostain.wordle_backend.game.common.util.GameMode;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;

public interface GameModeStrategy {
    LetterStatus[] check(Long id, String attempt, Level level);
    GameMode getGameMode();
}
