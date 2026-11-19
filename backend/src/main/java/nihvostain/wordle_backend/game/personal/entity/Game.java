package nihvostain.wordle_backend.game.personal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nihvostain.wordle_backend.auth.user.User;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.model.GameStatus;
import nihvostain.wordle_backend.game.personal.model.Mode;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "target_word", nullable = false)
    private String targetWord;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Version
    private long version;

    public Game(User user, String targetWord, Mode mode, Level level, GameStatus status) {
        this.user = user;
        this.targetWord = targetWord;
        this.mode = mode;
        this.level = level;
        this.status = status;
        this.startedAt = LocalDateTime.now();
    }

}
