package nihvostain.wordle_backend.auth.user;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findUserByLogin(String login);
    boolean existsUserByLogin(String login);
    void save(User user);
    User getReferenceById(Long id);
}
