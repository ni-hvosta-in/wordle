package nihvostain.wordle_backend.game.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nihvostain.wordle_backend.game.Level;
import nihvostain.wordle_backend.user.User;


@Entity
public class UserGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    private User user;
    private int a1Index;
    private int a2Index;
    private int b1Index;
    private int b2Index;

    public UserGame(User user){
        this.user = user;
    }

    public UserGame() {

    }

    public int getIndexByLevel(Level level){

        return switch (level){
            case A1 -> a1Index;
            case A2 -> a2Index;
            case B1 -> b1Index;
            case B2 -> b2Index;
        };

    }

    public void setIndexByLevel(Level level, int index){
        switch (level){
            case A1 -> a1Index = index;
            case A2 -> a2Index = index;
            case B1 -> b1Index = index;
            case B2 -> b2Index = index;
        }
    }
}
