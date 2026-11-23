package nihvostain.wordle_backend.game.personal;

import nihvostain.wordle_backend.game.common.util.Level;
import nihvostain.wordle_backend.game.personal.model.GameStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonalGameConcurrencyIT extends AbstractPersonalGameIntegrationTest {

    private static final int NUM_THREADS = 10;

    @Test
    void shouldAwardCoinsOnce_whenTargetWordIsCheckedConcurrently() throws Exception {
        TestUser tu = registerAndLogin();
        String token = tu.token();
        Level level = Level.A1;

        check(token, DUMMY_WORD, level);

        Long userId = userId(tu.login());
        String target = singleGame(userId, level).getTargetWord();
        if (target.equals(DUMMY_WORD)) {
            // the first game is already won, the next check starts a new one
            check(token, DUMMY_WORD, level);
            target = singleGame(userId, level, GameStatus.IN_PROGRESS).getTargetWord();
        }
        int coinsBefore = coins(userId);

        List<Integer> statuses = checkConcurrently(token, target, level);

        assertTrue(statuses.stream().allMatch(s -> s == 200 || s == 409),
                "only 200 or 409 expected, got " + statuses);
        assertTrue(statuses.contains(200), "at least one request must succeed, got " + statuses);
        assertEquals(coinsBefore + 10, coins(userId));
        singleGame(userId, level, GameStatus.WON);
    }

    private List<Integer> checkConcurrently(String token, String attempt, Level level) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        try {
            CountDownLatch ready = new CountDownLatch(NUM_THREADS);
            CountDownLatch start = new CountDownLatch(1);

            List<Future<Integer>> futures = new ArrayList<>(NUM_THREADS);
            for (int i = 0; i < NUM_THREADS; i++) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    start.await();
                    return checkResponse(token, attempt, level).getStatus();
                }));
            }

            assertTrue(ready.await(5, TimeUnit.SECONDS), "not all threads reached the start");
            start.countDown();

            List<Integer> statuses = new ArrayList<>(NUM_THREADS);
            for (Future<Integer> future : futures) {
                statuses.add(future.get(10, TimeUnit.SECONDS));
            }
            return statuses;
        } finally {
            executor.shutdownNow();
        }
    }
}
