package nihvostain.wordle_backend.game.services;

import jakarta.annotation.PostConstruct;
import nihvostain.wordle_backend.game.LetterStatus;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.game.model.Attempt;
import nihvostain.wordle_backend.game.model.LevelAttempt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSessionService {

    private final Map<Long, LevelAttempt> sessions = new ConcurrentHashMap<>();

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
}
