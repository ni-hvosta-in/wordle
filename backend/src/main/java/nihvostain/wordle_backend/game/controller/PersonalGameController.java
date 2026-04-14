package nihvostain.wordle_backend.game.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nihvostain.wordle_backend.game.GameMode;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.game.dto.GameRequest;
import nihvostain.wordle_backend.game.dto.GameResponse;
import nihvostain.wordle_backend.game.dto.UserAttempts;
import nihvostain.wordle_backend.game.services.GameService;
import nihvostain.wordle_backend.game.services.GameSessionService;
import nihvostain.wordle_backend.game.services.UserProgressService;
import nihvostain.wordle_backend.game.services.WordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/game/personal")
public class PersonalGameController {

    @Value("${game.attempt.count}")
    private int MAX_ATTEMPTS;

    private final WordService wordService;
    private final GameService gameService;
    private final GameSessionService gameSessionService;
    private final UserProgressService userProgressService;

    PersonalGameController(WordService wordService,
                           GameService gameService,
                           GameSessionService gameSessionService,
                           UserProgressService userProgressService) {
        this.wordService = wordService;
        this.gameService = gameService;
        this.gameSessionService = gameSessionService;
        this.userProgressService = userProgressService;
    }

    @PostMapping("/check")
    public ResponseEntity<GameResponse> checkPersonalWord(@Valid @RequestBody GameRequest gameRequest,
                                                          HttpServletRequest request){
        Long userID = getUserID(request);
        return ResponseEntity.ok(new GameResponse(gameService.check(GameMode.PERSONAL, userID, gameRequest.attempt(), gameRequest.level())));
    }

    @GetMapping("/attempts")
    public ResponseEntity<UserAttempts> getUserAttempts(Level level, HttpServletRequest request){
        Long userID = getUserID(request);
        return ResponseEntity.ok(new UserAttempts(gameSessionService.getAttemptsByLevel(userID, level)));
    }

    @GetMapping("/word")
    public ResponseEntity<String> getWord(Level level, HttpServletRequest request){
        Long userID = getUserID(request);

        int index = userProgressService.getUserIndex(userID, level);
        String word = wordService.getPersonalWord(level, index - 1);

        return ResponseEntity.ok(word);
    }

    private Long getUserID(HttpServletRequest request){
        return (Long) request.getAttribute("userID");
    }

}
