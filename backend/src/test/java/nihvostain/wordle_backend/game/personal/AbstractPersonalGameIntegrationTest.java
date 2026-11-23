package nihvostain.wordle_backend.game.personal;

import com.jayway.jsonpath.JsonPath;
import nihvostain.wordle_backend.TestcontainersConfiguration;
import nihvostain.wordle_backend.auth.user.UserRepository;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.dto.ProfileInfo;
import nihvostain.wordle_backend.game.personal.entity.Game;
import nihvostain.wordle_backend.game.personal.model.GameStatus;
import nihvostain.wordle_backend.game.personal.model.Mode;
import nihvostain.wordle_backend.game.personal.repository.GameRepository;
import nihvostain.wordle_backend.game.personal.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
abstract class AbstractPersonalGameIntegrationTest {

    protected static final String PASSWORD = "password";
    protected static final int MAX_ATTEMPTS = 6;
    protected static final String DUMMY_WORD = "WRONG";
    protected static final String VALID_CHECK_BODY = """
            {"attempt": "APPLE", "level": "A1"}
            """;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected GameRepository gameRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserProfileRepository userProfileRepository;



    protected record TestUser(String login, String token) {}

    protected TestUser registerAndLogin() throws Exception {
        String login = "user-" + UUID.randomUUID();
        register(login);
        return new TestUser(login, login(login));
    }

    protected LetterStatus[] check(String token, String attempt, Level level) throws Exception {
        String body = mockMvc.perform(post("/game/personal/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"attempt": "%s", "level": "%s"}
                                """.formatted(attempt, level)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<String> statuses = JsonPath.read(body, "$.statuses");
        return statuses.stream()
                .map(LetterStatus::valueOf)
                .toArray(LetterStatus[]::new);
    }

    protected String register(String login) throws Exception {
        return requestToken("/auth/register", login);
    }

    protected String login(String login) throws Exception {
        return requestToken("/auth/login", login);
    }

    protected String requestToken(String path, String login) throws Exception {
        String body = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"login": "%s", "password": "%s"}
                            """.formatted(login, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(body, "$.token");
    }

    protected ProfileInfo profile(String token, Level level) throws Exception {
        String body = mockMvc.perform(get("/game/personal/profile")
                        .header("Authorization", "Bearer " + token)
                        .param("level", level.name()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return new ProfileInfo(JsonPath.read(body, "$.gameLevel"), JsonPath.read(body, "$.coins"));
    }

    protected static LetterStatus[] allCorrect() {
        LetterStatus[] statuses = new LetterStatus[5];
        Arrays.fill(statuses, LetterStatus.CORRECT);
        return statuses;
    }

    protected int coins(Long userId) {
        return userProfileRepository.findByUserId(userId).orElseThrow().getCoins();
    }

    protected Long userId(String login) {
        return userRepository.findUserByLogin(login).orElseThrow().getId();
    }

    protected Game singleGame(Long userId, Level level) {
        List<Game> games = gameRepository.findByUserIdAndLevelAndMode(userId, level, Mode.PERSONAL);
        assertEquals(1, games.size(), "expected exactly one game");
        return games.get(0);
    }

    protected Game singleGame(Long userId, Level level, GameStatus status) {
        List<Game> games = gameRepository.findByUserIdAndLevelAndModeAndStatus(
                userId, level, Mode.PERSONAL, status);
        assertEquals(1, games.size(), "expected exactly one " + status + " game");
        return games.get(0);
    }

    protected MockHttpServletResponse checkResponse(String token, String attempt, Level level) throws Exception {
        return mockMvc.perform(post("/game/personal/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                            {"attempt": "%s", "level": "%s"}
                            """.formatted(attempt, level)))
                .andReturn()
                .getResponse();
    }


}
