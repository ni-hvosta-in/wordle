package nihvostain.wordle_backend.common.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nihvostain.wordle_backend.common.services.JWTService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    JWTService jwtService;
    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader("Authorization");

        String origin = request.getHeader("Origin");
        if (auth == null || !auth.startsWith("Bearer ")) {
            addCorsHeaders(response, origin);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = auth.substring(7);

        try {
            Long id = jwtService.extractUserId(token);
            request.setAttribute("userID", id);
        } catch (JWTVerificationException | NumberFormatException e) {
            addCorsHeaders(response, origin);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return !path.startsWith("/game/personal");
    }

    private void addCorsHeaders(HttpServletResponse response, String origin) {

        if ("http://localhost:5173".equals(origin) ||
                "http://localhost:3000".equals(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
    }

}
