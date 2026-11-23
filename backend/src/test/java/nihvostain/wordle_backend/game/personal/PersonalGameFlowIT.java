package nihvostain.wordle_backend.game.personal;

import nihvostain.wordle_backend.game.common.util.LetterStatus;
import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.model.GameStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PersonalGameFlowIT extends AbstractPersonalGameIntegrationTest {

    @Test
    void shouldReturnFiveStatuses_whenAuthorizedUserChecksWord() throws Exception {
        TestUser tu = registerAndLogin();

        LetterStatus[] statuses = check(tu.token(), "APPLE", Level.A1);

        assertEquals(5, statuses.length);
    }

    @Test
    void shouldReturnUnauthorized_whenCheckWithoutAuthorizationHeader() throws Exception {
        mockMvc.perform(post("/game/personal/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CHECK_BODY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorized_whenCheckWithInvalidToken() throws Exception {
        mockMvc.perform(post("/game/personal/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer not-a-jwt")
                        .content(VALID_CHECK_BODY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldWinGame_whenUserGuessesTargetWord() throws Exception {

        TestUser tu = registerAndLogin();
        String login = tu.login();
        Level level = Level.A1;
        String token = tu.token();

        check(token, DUMMY_WORD, level);

        Long userId = userId(login);
        String target = singleGame(userId, level).getTargetWord();

        if (!target.equals(DUMMY_WORD)) {
            LetterStatus[] statuses = check(token, target, level);
            assertArrayEquals(allCorrect(), statuses);
        }

        assertEquals(10, coins(userId));
        singleGame(userId, level, GameStatus.WON);
    }

    @Test
    void shouldLoseGame_whenAllAttemptsAreWrong() throws Exception {
        TestUser tu = registerAndLogin();
        String login = tu.login();
        Level level = Level.A1;
        String token = tu.token();

        check(token, DUMMY_WORD, level);

        Long userId = userId(login);
        boolean firstGameWon = singleGame(userId, level).getTargetWord().equals(DUMMY_WORD);
        // a won game is closed, the next check starts a new one whose target is never an already played word
        int remainingAttempts = firstGameWon ? MAX_ATTEMPTS : MAX_ATTEMPTS - 1;
        int coinsBefore = coins(userId);

        for (int attempt = 0; attempt < remainingAttempts; attempt++) {
            check(token, DUMMY_WORD, level);
        }

        assertEquals(coinsBefore, coins(userId));
        singleGame(userId, level, GameStatus.LOST);
    }



}
