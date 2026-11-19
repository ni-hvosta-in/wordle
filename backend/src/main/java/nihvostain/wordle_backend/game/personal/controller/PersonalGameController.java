package nihvostain.wordle_backend.game.personal.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nihvostain.wordle_backend.game.common.util.GameMode;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.common.dto.GameRequest;
import nihvostain.wordle_backend.game.common.dto.GameResponse;
import nihvostain.wordle_backend.game.common.services.GameStrategyService;
import nihvostain.wordle_backend.game.personal.dto.Clue;
import nihvostain.wordle_backend.game.personal.dto.ProfileInfo;
import nihvostain.wordle_backend.game.personal.dto.UserAttempts;
import nihvostain.wordle_backend.game.personal.services.GameService;
import nihvostain.wordle_backend.game.personal.services.GameSessionService;
import nihvostain.wordle_backend.game.personal.services.PersonalService;
import nihvostain.wordle_backend.game.personal.services.UserProfileService;
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

    private final GameStrategyService gameStrategyService;
    private final GameService gameService;
    private final UserProfileService userProfileService;
    private final PersonalService personalService;
    private final GameSessionService gameSessionService;

    PersonalGameController(WordService wordService,
                           GameStrategyService gameStrategyService,
                           UserProfileService userProfileService,
                           GameService gameService,
                           PersonalService personalService,
                           GameSessionService gameSessionService) {

        this.gameStrategyService = gameStrategyService;
        this.gameService = gameService;
        this.userProfileService = userProfileService;
        this.personalService = personalService;
        this.gameSessionService = gameSessionService;
    }

    @PostMapping("/check")
    public ResponseEntity<GameResponse> checkPersonalWord(@Valid @RequestBody GameRequest gameRequest,
                                                          HttpServletRequest request){
        Long userID = getUserID(request);
        return ResponseEntity.ok(
                new GameResponse(
                        gameStrategyService.check(GameMode.PERSONAL, userID, gameRequest.attempt(), gameRequest.level())
                )
        );
    }

    @GetMapping("/attempts")
    public ResponseEntity<UserAttempts> getUserAttempts(Level level, HttpServletRequest request){
        Long userID = getUserID(request);
        return ResponseEntity.ok(new UserAttempts(gameService.userLevelAttempts(userID, level)));
    }

    @GetMapping("/word")
    public ResponseEntity<String> getWord(Level level, HttpServletRequest request){
        Long userID = getUserID(request);
        return ResponseEntity.ok(gameService.currTargetWorldByUserAndLevel(userID, level));
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileInfo> getProfileInfo(Level level, HttpServletRequest request){

        Long userID = getUserID(request);
        return ResponseEntity.ok(userProfileService.getUserProfileInfo(userID, level));
    }

    private Long getUserID(HttpServletRequest request){
        return (Long) request.getAttribute("userID");
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



}
