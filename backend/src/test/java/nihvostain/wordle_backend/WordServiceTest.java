package nihvostain.wordle_backend;

import nihvostain.wordle_backend.game.common.services.WordChecker;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;
class WordServiceTest {

    private final WordChecker wordChecker = new WordChecker();

    @ParameterizedTest(name = "{index} {0}, {1} needed {2}")
    @CsvFileSource(resources = "/word-checker/cases.csv", numLinesToSkip = 1)
    void shouldColorLettersByWordleRules(String attempt, String target, String targetStatuses) {
        LetterStatus [] currStatuses = wordChecker.checkWord(attempt, target);

        char [] chars = new char[currStatuses.length];
        for (int i = 0; i < currStatuses.length; i++) {
            switch (currStatuses[i]){
                case WRONG -> chars[i] = 'W';
                case INCLUDES -> chars[i] = 'I';
                case CORRECT -> chars[i] = 'C';
                case UNUSED -> chars[i] = 'U';
            }
        }

        String statuses = String.valueOf(chars);

        assertEquals(targetStatuses, statuses);
    }
}
