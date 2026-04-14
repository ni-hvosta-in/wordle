package nihvostain.wordle_backend.game.model;

import nihvostain.wordle_backend.game.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LevelAttempt {

    private final Map<Level, ArrayList<Attempt>> levelAttempts = new HashMap<>();

    public void addAttempt(Level level, Attempt attempt) {
        ArrayList<Attempt> attempts = levelAttempts.getOrDefault(level, new ArrayList<>());
        attempts.add(attempt);
        levelAttempts.put(level, attempts);
    }

    public void removeAttempt(Level level, Attempt attempt) {
        ArrayList<Attempt> attempts = levelAttempts.get(level);
        attempts.remove(attempt);
    }

    public ArrayList<Attempt> getAttempts(Level level) {
        return levelAttempts.get(level);
    }

    @Override
    public String toString() {
        return levelAttempts.toString();
    }
}
