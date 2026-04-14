package nihvostain.wordle_backend.game.services;

import nihvostain.wordle_backend.game.LetterStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class WordChecker {
    public LetterStatus [] checkWord(String attempt, String target) {

        char [] targetChars = target.toCharArray();
        char [] attemptChars = attempt.toCharArray();
        Map<Character, Integer> letterCount = new HashMap<>();

        for (char ch : targetChars) {
            letterCount.put(ch, letterCount.getOrDefault(ch, 0) + 1);
        }

        System.out.println(letterCount);
        LetterStatus [] statuses = new LetterStatus [attempt.length()];
        Arrays.fill(statuses, LetterStatus.WRONG);

        for (int i = 0; i < attemptChars.length; i++) {

            if (letterCount.containsKey(attemptChars[i]) && letterCount.get(attemptChars[i]) > 0) {
                if (targetChars[i] == attemptChars[i]) {
                    statuses[i] = LetterStatus.CORRECT;
                } else {
                    statuses[i] = LetterStatus.INCLUDES;
                }
                letterCount.put(attemptChars[i], letterCount.get(attemptChars[i]) - 1);
            }
            if (!letterCount.containsKey(attemptChars[i])) {
                statuses[i] = LetterStatus.WRONG;
            }
        }

        return statuses;
    }
}
