package nihvostain.wordle_backend.game.services;

import nihvostain.wordle_backend.game.LetterStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WordChecker {
    public String [] checkWord(String attempt, String target) {

        char [] targetChars = target.toCharArray();
        char [] attemptChars = attempt.toCharArray();
        Map<Character, Integer> letterCount = new HashMap<>();

        for (char ch : targetChars) {
            letterCount.put(ch, letterCount.getOrDefault(ch, 0) + 1);
        }

        System.out.println(letterCount);
        String [] statues = new String [attempt.length()];

        for (int i = 0; i < attemptChars.length; i++) {

            if (letterCount.containsKey(attemptChars[i]) && letterCount.get(attemptChars[i]) > 0) {
                if (targetChars[i] == attemptChars[i]) {
                    statues[i] = LetterStatus.CORRECT.getStatus();
                } else {
                    statues[i] = LetterStatus.INCLUDES.getStatus();
                }
                letterCount.put(attemptChars[i], letterCount.get(attemptChars[i]) - 1);
            }
            if (!letterCount.containsKey(attemptChars[i])) {
                statues[i] = LetterStatus.WRONG.getStatus();
            }
        }

        return statues;
    }
}
