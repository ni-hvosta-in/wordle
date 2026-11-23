package nihvostain.wordle_backend.game.personal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nihvostain.wordle_backend.auth.user.User;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_guess_game_attempt", columnNames = {"game_id", "attempt_number"}))
public class Guess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "attempt_number")
    private int attemptNumber;

    @Column(nullable = false)
    private String attempt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private LetterStatus[] statuses;

    @Column(name = "guessed_at")
    private LocalDateTime guessedAt;

    public Guess(Game game, int attemptNumber, String attempt, LetterStatus[] statuses, LocalDateTime guessedAt) {
        this.game = game;
        this.attemptNumber = attemptNumber;
        this.attempt = attempt;
        this.statuses = statuses;
        this.guessedAt = guessedAt;
    }
}
