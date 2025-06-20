package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtComponent {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    @Value("${auth.jwt.expiration}")
    private long jwtExpiration;

    public String generateJwtToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
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
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        System.out.println(jwtSecret);
        byte[] key = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(key);
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
