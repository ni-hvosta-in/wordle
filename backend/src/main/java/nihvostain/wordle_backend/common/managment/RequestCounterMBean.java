package nihvostain.wordle_backend.common.managment;

public interface RequestCounterMBean {

    int getTotalRequests();

    int getFailedRequests();

    double getErrorRate();
}