package nihvostain.wordle_backend.game.common.modes;

import nihvostain.wordle_backend.game.common.util.GameMode;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.entity.Game;
import nihvostain.wordle_backend.game.personal.model.GameStatus;
import nihvostain.wordle_backend.game.personal.services.GameService;
import nihvostain.wordle_backend.game.personal.services.UserProfileService;
import nihvostain.wordle_backend.game.common.services.WordChecker;
import nihvostain.wordle_backend.game.common.services.WordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class PersonalGameStrategy implements GameModeStrategy {

    @Value("${game.attempt.count}")
    private int MAX_ATTEMPTS;

    private final WordChecker wordChecker;
    private final UserProfileService userProfileService;

    private final GameService gameService;

    public PersonalGameStrategy(WordChecker wordChecker,
                                UserProfileService userProfileService,
                                GameService gameService) {

        this.wordChecker = wordChecker;
        this.userProfileService = userProfileService;
        this.gameService = gameService;
    }

    @Override
    @Transactional
    public LetterStatus[] check(Long id, String attempt, Level level) {

        Game userGame = gameService.currentUserTargetWordByLevel(id, level);

        LetterStatus [] statuses = wordChecker.checkWord(attempt, userGame.getTargetWord());

        int k = gameService.addGuess(userGame, attempt, statuses);

        if (Arrays.stream(statuses).allMatch((status) -> status.equals(LetterStatus.CORRECT))
                || k >= MAX_ATTEMPTS - 1) {

            if (Arrays.stream(statuses).allMatch((status) -> status.equals(LetterStatus.CORRECT))){
                gameService.closeGame(userGame, GameStatus.WON);
                userProfileService.correctWord(id);
                return statuses;
            }

            gameService.closeGame(userGame, GameStatus.LOST);
        }

        return statuses;
    }


    @Override
    public GameMode getGameMode() {
        return GameMode.PERSONAL;
    }
}
