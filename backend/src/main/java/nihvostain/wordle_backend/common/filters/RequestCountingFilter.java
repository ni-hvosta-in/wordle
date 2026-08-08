package nihvostain.wordle_backend.common.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nihvostain.wordle_backend.common.managment.RequestCounter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestCountingFilter extends OncePerRequestFilter {

    private final RequestCounter counter;

    public RequestCountingFilter(RequestCounter counter) {
        this.counter = counter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        counter.incrementRequests();

        filterChain.doFilter(request, response);

        if (response.getStatus() >= 400){
            counter.incrementFailedRequests();
        }
    }
}
