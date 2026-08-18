package nihvostain.wordle_backend.game.personal.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nihvostain.wordle_backend.game.common.util.GameMode;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.common.dto.GameRequest;
import nihvostain.wordle_backend.game.common.dto.GameResponse;
import nihvostain.wordle_backend.game.common.services.GameService;
import nihvostain.wordle_backend.game.personal.dto.Clue;
import nihvostain.wordle_backend.game.personal.dto.ProfileInfo;
import nihvostain.wordle_backend.game.personal.dto.UserAttempts;
import nihvostain.wordle_backend.game.personal.services.GameSessionService;
import nihvostain.wordle_backend.game.personal.services.PersonalService;
import nihvostain.wordle_backend.game.personal.services.UserProgressService;
import nihvostain.wordle_backend.game.common.services.WordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/game/personal")
public class PersonalGameController {

    @Value("${game.attempt.count}")
    private int MAX_ATTEMPTS;

    private final WordService wordService;
    private final GameService gameService;
    private final GameSessionService gameSessionService;
    private final UserProgressService userProgressService;
    private final PersonalService personalService;
    PersonalGameController(WordService wordService,
                           GameService gameService,
                           GameSessionService gameSessionService,
                           UserProgressService userProgressService,
                           PersonalService personalService) {
        this.wordService = wordService;
        this.gameService = gameService;
        this.gameSessionService = gameSessionService;
        this.userProgressService = userProgressService;
        this.personalService = personalService;
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

    @GetMapping("/profile")
    public ResponseEntity<ProfileInfo> getProfileInfo(Level level, HttpServletRequest request){

        Long userID = getUserID(request);
        return ResponseEntity.ok(userProgressService.getUserProfileInfo(userID, level));
    }

    @GetMapping("/clue")
    public ResponseEntity<Clue> getClue(Level level, HttpServletRequest request){
        Long userID = getUserID(request);

        return ResponseEntity.ok(personalService.getClueByLevel(userID, level));
    }

    @GetMapping("/clues")
    public ResponseEntity<List<Clue>> getClues(Level level, HttpServletRequest request){
        Long userID = getUserID(request);

        return ResponseEntity.ok(gameSessionService.getCluesByLevel(userID, level));
    }
    private Long getUserID(HttpServletRequest request){
        return (Long) request.getAttribute("userID");
    }

}
