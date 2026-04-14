package nihvostain.wordle_backend.game.controller;

import jakarta.validation.Valid;
import nihvostain.wordle_backend.game.GameMode;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.game.dto.GameRequest;
import nihvostain.wordle_backend.game.dto.GameResponse;
import nihvostain.wordle_backend.game.services.GameService;
import nihvostain.wordle_backend.game.services.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game/daily")
public class DailyGameController {

    private final WordService wordService;
    private final GameService gameService;

    public DailyGameController(WordService wordService, GameService gameService) {
        this.wordService = wordService;
        this.gameService = gameService;
    }

    @PostMapping("/check")
    public ResponseEntity<GameResponse> checkDailyWord(@Valid @RequestBody GameRequest gameRequest){
        return ResponseEntity.ok(new GameResponse(gameService.check(GameMode.DAILY, null,gameRequest.attempt(), gameRequest.level())));
    }

    @GetMapping("/word")
    public ResponseEntity<String> getDailyWord(@RequestParam Level level){
        return ResponseEntity.ok(wordService.getDailyWord(level));
    }


}
