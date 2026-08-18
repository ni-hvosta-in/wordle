package nihvostain.wordle_backend.game.common.modes;

import nihvostain.wordle_backend.game.common.util.GameMode;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.services.GameSessionService;
import nihvostain.wordle_backend.game.personal.services.UserProgressService;
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
    private final WordService wordService;
    private final GameSessionService gameSessionService;
    private final UserProgressService userProgressService;

    public PersonalGameStrategy(WordChecker wordChecker,
                                WordService wordService,
                                GameSessionService gameSessionService,
                                UserProgressService userProgressService) {

        this.wordChecker = wordChecker;
        this.wordService = wordService;
        this.gameSessionService = gameSessionService;
        this.userProgressService = userProgressService;

    }

    @Override
    @Transactional
    public LetterStatus[] check(Long id, String attempt, Level level) {

        int index = userProgressService.getUserIndex(id, level);
        String target = wordService.getPersonalWord(level, index);
        LetterStatus [] statuses = wordChecker.checkWord(attempt, target);

        gameSessionService.addAttempt(id, level, attempt, statuses);

        if (Arrays.stream(statuses).allMatch((status) -> status.equals(LetterStatus.CORRECT))
                || gameSessionService.getAttemptsByLevel(id, level).size() == MAX_ATTEMPTS) {

            userProgressService.incrementUserIndex(id, level);
            gameSessionService.clearAttempts(id, level);
            gameSessionService.clearClue(id, level);

            if (Arrays.stream(statuses).allMatch((status) -> status.equals(LetterStatus.CORRECT))){
                userProgressService.correctWord(id);
            }
        }

        return statuses;
    }


    @Override
    public GameMode getGameMode() {
        return GameMode.PERSONAL;
    }
}
