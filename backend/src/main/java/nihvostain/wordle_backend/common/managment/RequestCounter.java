package nihvostain.wordle_backend.common.managment;

import org.springframework.stereotype.Component;

@Component
public class RequestCounter implements RequestCounterMBean {

    private int totalRequests = 0;
    private int failedRequests = 0;

    public synchronized void incrementRequests() {
        totalRequests++;
    }

    public synchronized void incrementFailedRequests() {
        failedRequests++;
    }

    @Override
    public int getTotalRequests() {
        return totalRequests;
    }

    @Override
    public int getFailedRequests() {
        return failedRequests;
    }

    @Override
    public double getErrorRate() {
        if (totalRequests == 0) {
            return 0;
        }

        return (double) failedRequests / totalRequests;
    }
}