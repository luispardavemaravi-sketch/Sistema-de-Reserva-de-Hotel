package pe.edu.utp.sistemadereservacionhotel.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;

    public JwtService(@Value("${JWT_SECRET}") String secret) {
        if (secret.length() < 32) throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 caracteres");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String issue(Usuario user) {
        long now = System.currentTimeMillis();
        return Jwts.builder().setSubject(user.getEmail()).setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 8 * 60 * 60 * 1000L))
                .signWith(key).compact();
    }

    public String subject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
