package nihvostain.wordle_backend.game.personal;

import com.jayway.jsonpath.JsonPath;
import nihvostain.wordle_backend.TestcontainersConfiguration;
import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class PersonalGameFlowIT {

    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkTest() throws Exception {
        String login = "user-" + UUID.randomUUID();
        register(login);
        String token = login(login);

        LetterStatus[] statuses = check(token, "APPLE", Level.A1);

        assertEquals(5, statuses.length);
    }

    private LetterStatus[] check(String token, String attempt, Level level) throws Exception {
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

    private String register(String login) throws Exception {
        return requestToken("/auth/register", login);
    }

    private String login(String login) throws Exception {
        return requestToken("/auth/login", login);
    }

    private String requestToken(String path, String login) throws Exception {
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
}
