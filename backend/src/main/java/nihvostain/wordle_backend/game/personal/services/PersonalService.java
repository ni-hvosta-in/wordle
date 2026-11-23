package nihvostain.wordle_backend.game.personal.services;

import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.dto.Clue;
import nihvostain.wordle_backend.game.personal.entity.Game;
import nihvostain.wordle_backend.game.personal.exception.NoClueException;
import nihvostain.wordle_backend.game.personal.model.Attempt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PersonalService {

    private final GameService gameService;

    public PersonalService(GameService gameService) {
        this.gameService = gameService;
    }

    // TODO #5: persist issued clues, pick a random unrevealed position and return them in getCluesByLevel
    public Clue getClueByLevel(Long userId, Level level) {
        Game game = gameService.findActiveGame(userId, level)
                .orElseThrow(() -> new NoClueException("No active game on level %s".formatted(level)));

        String target = game.getTargetWord();
        Set<Integer> guessedPositions = correctPositions(gameService.userLevelAttempts(userId, level));

        for (int i = 0; i < target.length(); i++) {
            if (!guessedPositions.contains(i)) {
                return new Clue(i, target.charAt(i));
            }
        }
        throw new NoClueException("All letters are already guessed");
    }

    public List<Clue> getCluesByLevel(Long userId, Level level) {
        return List.of();
    }

    private static Set<Integer> correctPositions(List<Attempt> attempts) {
        Set<Integer> positions = new HashSet<>();
        for (Attempt attempt : attempts) {
            LetterStatus[] statuses = attempt.statuses();
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i] == LetterStatus.CORRECT) {
                    positions.add(i);
                }
            }
        }
        return positions;
    }
}
