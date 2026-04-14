package nihvostain.wordle_backend.game.services;

import jakarta.annotation.PostConstruct;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class WordService {

    private final HashMap<Level, ArrayList<String>> wordsToGenerate = new HashMap<>();
    private final Set<String> dictionary = new HashSet<>();

    @Value("${game.seed}")
    private String seed;

    @PostConstruct
    void init() throws FileNotFoundException {
        for (Level level: Level.values()){
            loadWords(level);
            System.out.printf("load %s level\n", level);
        }
    }

    private void shuffled(ArrayList<String> words){
        long seed = Long.parseLong(this.seed);
        Random random = new Random(seed);
        Collections.shuffle(words, random);
    }

    private void loadWords(Level level) throws FileNotFoundException {

        String fileName = "words/%s.txt".formatted(level.toString().toLowerCase());
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (is == null) {
            throw new RuntimeException("File not found in resources: " + fileName);
        }

        Scanner scanner = new Scanner(is);
        ArrayList<String> words = new ArrayList<>();
        while (scanner.hasNextLine()){
            String word = scanner.nextLine().trim();
            dictionary.add(word);
            words.add(word);
        }
        shuffled(words);
        wordsToGenerate.put(level, words);
    }

    public String getDailyWord(Level level) {

        List<String> words = wordsToGenerate.get(level);

        long day = LocalDate.now(ZoneOffset.UTC).toEpochDay();
        Random random = new Random(day);
        return words.get(random.nextInt(words.size()));
    }

    public String getPersonalWord(Level level, int index){
        return wordsToGenerate.get(level).get(index);
    }

    public boolean dictionaryContains(String word) {
        return dictionary.contains(word);
    }


}
