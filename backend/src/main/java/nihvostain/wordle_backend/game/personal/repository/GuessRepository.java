package nihvostain.wordle_backend.game.personal.repository;

import nihvostain.wordle_backend.game.personal.entity.Game;
import nihvostain.wordle_backend.game.personal.entity.Guess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuessRepository extends JpaRepository<Guess, Long> {
    List<Guess> findByGame(Game game);
}
