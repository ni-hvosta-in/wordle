package nihvostain.wordle_backend.game;

import jakarta.validation.Valid;
import nihvostain.wordle_backend.game.dto.GameRequest;
import nihvostain.wordle_backend.game.dto.GameResponse;
import nihvostain.wordle_backend.game.services.GameService;
import nihvostain.wordle_backend.game.services.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    WordService wordService;
    GameService gameService;

    public GameController(WordService wordService, GameService gameService) {
        this.wordService = wordService;
        this.gameService = gameService;
    }

    @PostMapping("/daily/check")
    public ResponseEntity<GameResponse> getDailyWord(@Valid @RequestBody GameRequest gameRequest){
        return ResponseEntity.ok(new GameResponse(gameService.checkDaily(gameRequest.attempt(), gameRequest.level())));
    }

    @GetMapping("/daily/word")
    public ResponseEntity<String> getDailyWordWord(@RequestParam Level level){
        return ResponseEntity.ok(wordService.getDailyWord(level));
    }
}
