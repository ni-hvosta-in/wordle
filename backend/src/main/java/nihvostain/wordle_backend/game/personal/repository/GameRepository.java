package nihvostain.wordle_backend.game.personal.repository;

import nihvostain.wordle_backend.auth.user.User;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.entity.Game;
import nihvostain.wordle_backend.game.personal.model.GameStatus;
import nihvostain.wordle_backend.game.personal.model.Mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByUserIdAndLevelAndMode(Long userId, Level level, Mode mode);
    List<Game> findByUserIdAndLevelAndModeAndStatus(Long userId, Level level, Mode mode, GameStatus status);

    int countGameByUserIdAndLevel(Long userId, Level level);

    Set<GameStatus> FINISHED_STATUSES = Set.of(GameStatus.WON, GameStatus.LOST);

    Optional<Game> findFirstByUserIdAndLevelAndModeAndStatusInOrderByFinishedAtDesc(
            Long userId, Level level, Mode mode, Collection<GameStatus> statuses);

    default Optional<Game> findLastFinishedGame(Long userId, Level level, Mode mode) {
        return findFirstByUserIdAndLevelAndModeAndStatusInOrderByFinishedAtDesc(
                userId, level, mode, FINISHED_STATUSES);
    }
}
