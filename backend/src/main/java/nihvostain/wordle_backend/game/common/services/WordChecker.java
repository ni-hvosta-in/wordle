package nihvostain.wordle_backend.game.common.services;

import nihvostain.wordle_backend.game.common.util.LetterStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class WordChecker {
    public LetterStatus [] checkWord(String attempt, String target) {

        char [] attemptChars = attempt.toCharArray();
        char [] targetChars = target.toCharArray();
        int n = targetChars.length;
        Map<Character, Integer> letterCount = new HashMap<>();

        for (char ch : targetChars) {
            letterCount.put(ch, letterCount.getOrDefault(ch, 0) + 1);
        }

        LetterStatus [] statuses = new LetterStatus[attempt.length()];

        for (int i = 0; i < n; i++) {
            char attemptChar = attemptChars[i];
            if (!letterCount.containsKey(attemptChar)) {
                statuses[i] = LetterStatus.WRONG;
                continue;
            }

            if (letterCount.get(attemptChar) > 0 && attemptChar == targetChars[i]) {
                statuses[i] = LetterStatus.CORRECT;
                letterCount.put(attemptChar, letterCount.get(attemptChar) - 1);
            }
        }

        for (int i = 0; i < attempt.length(); i++) {

            if (statuses[i] != null) {
                continue;
            }

            char attemptChar = attemptChars[i];
            if (letterCount.containsKey(attemptChar) && letterCount.get(attemptChar) != 0) {
                statuses[i] = LetterStatus.INCLUDES;
                letterCount.put(attemptChar, letterCount.get(attemptChar) - 1);
            } else {
                statuses[i] = LetterStatus.WRONG;
            }

        }

        return statuses;
    }
}
