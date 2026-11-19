package nihvostain.wordle_backend.game.personal.repository;

import nihvostain.wordle_backend.auth.user.User;
import nihvostain.wordle_backend.game.personal.entity.UserProfile;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserProfileRepository extends Repository<UserProfile, Long> {
    void save(UserProfile userProfile);
    boolean existsByUser(User user);
    Optional<UserProfile> findByUserId(Long id);
}
