package nihvostain.wordle_backend.game.personal.services;

import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.common.services.WordService;
import nihvostain.wordle_backend.game.personal.dto.Clue;
import nihvostain.wordle_backend.game.personal.exception.NoClueException;
import nihvostain.wordle_backend.game.personal.model.Attempt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonalService {

    private final GameSessionService gameSessionService;
    private final WordService wordService;
    private final UserProfileService userProfileService;
    public PersonalService(GameSessionService gameSessionService, WordService wordService, UserProfileService userProfileService) {
        this.gameSessionService = gameSessionService;
        this.wordService = wordService;
        this.userProfileService = userProfileService;
    }

    public Clue getClueByLevel(long userId, Level level) {

        int index = 0; // заглушка чтобы потом переписать
        String target = wordService.getPersonalWord(level, index);
        List<Attempt> attempts = gameSessionService.getAttemptsByLevel(userId, level);
        List<Clue> clues = gameSessionService.getCluesByLevel(userId, level);

        if (clues.size() == 5){
            throw new NoClueException("your clues is already taken");
        }
        Set<Integer> clueIds = clues.stream().map(Clue::index).collect(Collectors.toSet());

        System.out.println(attempts);
        System.out.println(clues);
        
        
        Set<Integer> correctStatuses = new HashSet<>();
        for (Attempt attempt : attempts) {
            for (int i = 0; i < attempt.statuses().length; i++){
                if (attempt.statuses()[i] == LetterStatus.CORRECT){
                    correctStatuses.add(i);
                }
            }
        }
        
        correctStatuses.addAll(clueIds);
        System.out.println("correct statuses: " + correctStatuses);

        List<Integer> badStatuses = new ArrayList<>();
        for (int i = 0; i < target.length(); i++){
            if (!correctStatuses.contains(i)){
                badStatuses.add(i);
            }
        }

        if (badStatuses.isEmpty()){
            throw new NoClueException("all letters are correct");
        }

        int idx = badStatuses.get(new Random().nextInt(badStatuses.size()));

        Clue clue = new Clue(idx, target.charAt(idx));
        gameSessionService.addClue(userId, level, clue);
        return clue;
    }
}
