package nihvostain.wordle_backend.common.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import nihvostain.wordle_backend.user.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    private final String secretKey = "secret";
    private final Algorithm algorithm = Algorithm.HMAC256(secretKey);
    private final JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("backend")
            .build();

    public String generateToken(User user){
        return JWT.create()
                .withSubject(Long.toString(user.getId()))
                .withClaim("login", user.getLogin())
                .withIssuer("backend")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token){
        return verifier.verify(token);
    }

    public Long extractUserId(String token){
        return Long.parseLong(verifyToken(token).getSubject());
    }

}
