package nihvostain.wordle_backend.game.personal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nihvostain.wordle_backend.auth.user.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    private User user;

    private int coins;

    public UserProfile(User user, int coins) {
        this.user = user;
        this.coins = coins;
    }
}
