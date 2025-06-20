package com.example.bankcards.security;

import com.example.bankcards.util.DecoderKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtComponent {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    @Value("${auth.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey key;

    @PostConstruct
    private void decryptedKey(){
        key = DecoderKey.fromBase64(jwtSecret).getSecretKey();
    }

    public String generateJwtToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }


    public String extractUserName(String token) {
        checkTokenOnEmpty(token);
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        checkTokenOnEmpty(token);
        return extractClaim(token, Claims::getExpiration);
    }

    public void checkTokenOnEmpty(String token) {
        if (token.isEmpty()) {
            throw new RuntimeException("Токен пустой");
        }
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolvers
    ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String login = extractUserName(token);
        return (
                (login.equals(userDetails.getUsername())) && !isTokenExpired(token)
        );
    }

    protected boolean isTokenExpired(String token) {
        try {
            extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}
