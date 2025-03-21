package antidimon.web.managervac.security.jwt;


import antidimon.web.managervac.repositories.MyUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenUtil {

    @Autowired
    private final MyUserRepository myUserRepository;
    @Value("${jwt}")
    private String secret;
    private static final long VALIDITY = TimeUnit.HOURS.toMillis(3);

    public JwtTokenUtil(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }


    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public boolean isValid(String jwt){
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(jwt).getPayload();
            return !claims.getExpiration().before(Date.from(Instant.now()));

        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String getEmail(String token){
        JwtParser parser =  Jwts.parser().verifyWith(getSecretKey()).build();
        return parser.parseSignedClaims(token).getPayload().getSubject();
    }

    public long getId(String token) {
        token = token.substring(7);
        String email = this.getEmail(token);
        return myUserRepository.findByEmail(email).get().getId();
    }
}
