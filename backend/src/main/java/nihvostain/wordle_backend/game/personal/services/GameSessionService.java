package nihvostain.wordle_backend.game.personal.services;

import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.dto.Clue;
import nihvostain.wordle_backend.game.personal.model.Attempt;
import nihvostain.wordle_backend.game.personal.model.LevelAttempt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSessionService {

    private final Map<Long, LevelAttempt> sessions = new ConcurrentHashMap<>();
    private final Map<Long, Map<Level, List<Clue>>> clues = new ConcurrentHashMap<>();

    public void addAttempt(Long id, Level level, String word, LetterStatus [] statuses) {
        LevelAttempt levelAttempt = sessions.computeIfAbsent(id, k -> new LevelAttempt());
        Attempt attempt = new Attempt(word, statuses);
        levelAttempt.addAttempt(level, attempt);
        System.out.println(sessions);
    }

    public ArrayList<Attempt> getAttemptsByLevel(Long id, Level level) {
        if (!sessions.containsKey(id)) {
            return new ArrayList<>();
        }
        return sessions.get(id).getAttempts(level);
    }

    public void clearAttempts(Long id, Level level) {
        sessions.get(id).getAttempts(level).clear();
    }

    public void addClue(Long id, Level level, Clue clue) {

        if (!clues.containsKey(id)) {
            clues.put(id, new HashMap<>());
            clues.get(id).put(level, new ArrayList<>());
        }

        clues.get(id).get(level).add(clue);

    }

    public void clearClue(Long id, Level level) {
        clues.get(id).get(level).clear();
    }

    public List<Clue> getCluesByLevel(Long id, Level level) {
        if (!clues.containsKey(id)) {
            return new ArrayList<>();
        }

        return clues.get(id).get(level);
    }
}
