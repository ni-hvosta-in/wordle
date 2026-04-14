package nihvostain.wordle_backend.user;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findUserByLogin(String login);
    boolean existsUserByLogin(String login);
    void save(User user);
}
