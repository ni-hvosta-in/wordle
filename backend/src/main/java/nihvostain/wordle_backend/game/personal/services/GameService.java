package nihvostain.wordle_backend.game.personal.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import nihvostain.wordle_backend.auth.user.UserRepository;
import nihvostain.wordle_backend.game.common.services.GameStrategyService;
import nihvostain.wordle_backend.game.common.services.WordService;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.entity.Game;
import nihvostain.wordle_backend.game.personal.entity.Guess;
import nihvostain.wordle_backend.game.personal.exception.FinishedGameNotFoundException;
import nihvostain.wordle_backend.game.personal.exception.GameInProgressException;
import nihvostain.wordle_backend.game.personal.model.Attempt;
import nihvostain.wordle_backend.game.personal.model.GameStatus;
import nihvostain.wordle_backend.game.personal.model.Mode;
import nihvostain.wordle_backend.game.personal.repository.GameRepository;
import nihvostain.wordle_backend.game.personal.repository.GuessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final WordService wordService;
    private final UserRepository userRepository;
    private final GuessRepository guessRepository;
    private final EntityManager entityManager;

    public GameService(GameRepository gameRepository,
                       WordService wordService,
                       UserRepository userRepository,
                       GuessRepository guessRepository,
                       EntityManager entityManager) {
        this.gameRepository = gameRepository;
        this.wordService = wordService;
        this.userRepository = userRepository;
        this.guessRepository = guessRepository;
        this.entityManager = entityManager;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Game currentUserTargetWordByLevel(Long id, Level level) {

        Game active = userActiveGameByLevel(id, level);

        if (active != null) {
            entityManager.lock(active, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            return active;
        }

        List<Game> games = gameRepository.findByUserIdAndLevelAndMode(id, level, Mode.PERSONAL);
        Set<String> words = games.stream().map(Game::getTargetWord).collect(Collectors.toSet());

        String targetWord = wordService.getPersonalWord(level, words);
        Game game = new Game(
                userRepository.getReferenceById(id),
                targetWord,
                Mode.PERSONAL,
                level, GameStatus.IN_PROGRESS
        );

        gameRepository.save(game);

        return game;

    }

    @Transactional(propagation = Propagation.MANDATORY)
    public int addGuess(Game game, String attempt, LetterStatus [] statuses){

        List<Guess> prev = guessRepository.findByGame(game);
        guessRepository.save(new Guess(game, prev.size() + 1, attempt, statuses, LocalDateTime.now()));

        return prev.size();

    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void closeGame(Game game, GameStatus status){
        game.setStatus(status);
        game.setFinishedAt(LocalDateTime.now());
        gameRepository.save(game);
    }

    public List<Attempt> userLevelAttempts(Long id, Level level){

        Game active = userActiveGameByLevel(id, level);

        if (active == null) {
            return new ArrayList<>();
        }

        List<Guess> guesses = guessRepository.findByGame(active);
        List<Attempt> attempts = new ArrayList<>();
        guesses.forEach(guess -> attempts.add(new Attempt(guess.getAttempt(), guess.getStatuses())));

        return attempts;
    }

    private Game userActiveGameByLevel(Long id, Level level){

        List<Game> userActive = gameRepository.findByUserIdAndLevelAndModeAndStatus(
                id,
                level,
                Mode.PERSONAL
                , GameStatus.IN_PROGRESS
        );

        if (!userActive.isEmpty()) {

            if (userActive.size() != 1) {
                throw new IllegalStateException("Found more than one active game");
            }

            return userActive.get(0);

        }

        return null;

    }

    public String currTargetWorldByUserAndLevel(Long id, Level level){
        Game active = userActiveGameByLevel(id, level);

        if (active != null) {
            throw new GameInProgressException("Word is not available while the game is in progress");
        }

        Optional<Game> lastGame = gameRepository.findLastFinishedGame(id, level, Mode.PERSONAL);

        return lastGame.orElseThrow(
                () -> new FinishedGameNotFoundException("No finished games on level %s".formatted(level))
        ).getTargetWord();

    }
    public int getLevelNumberByUserIdAndLevel(Long id, Level level){
        return gameRepository.countGameByUserIdAndLevel(id, level);
    }

}
