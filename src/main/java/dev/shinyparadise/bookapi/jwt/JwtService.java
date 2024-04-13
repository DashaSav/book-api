package dev.shinyparadise.bookapi.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    public static final String SECRET = "dAGgmx1eL4vsGoJem7r8HkrJKENpn4Yt7zq6faq2RgllsXEAzuMySNZxjSk1WmMwHea6KwYZRqoVBKd5IAaLPEGuimoQIOESjaMpVw7CIARB4Ph3MUdpzOQznQsJ2zLIrkA90Ut7lbu6lt7o74QLNjReKtyQ0U4nyrQmS7SI0SLMTVf4MpPPkDmtXqVIDE2MDcznxNmlw50Ly691ONui3HQCdcFLIjfwZ7SSvQl0NCZ3Pd5dThpMqVdx4lhqke1t";
    public static final long expirationTime = 1000L * 60 * 60 * 24 * 60;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .claims()
                .empty()
                .subject(userDetails.getUsername())
                .issuedAt(getIat())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .add(extraClaims)
                .and()
                .signWith(getSecretKey())
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Date getIat() {
        return new Date(System.currentTimeMillis());
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
