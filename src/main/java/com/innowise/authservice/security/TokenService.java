package com.innowise.authservice.security;

import com.innowise.authservice.exception.InvalidTokenException;
import com.innowise.authservice.model.dto.response.TokenResponse;
import com.innowise.authservice.model.entity.UserCredentials;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshIntervalMs;

    public String generateAccessToken(UserCredentials userCredentials) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userCredentials.getId().toString());
        claims.put("roles", List.of(userCredentials.getRole().name()));
        return createToken(claims, userCredentials.getLogin(), jwtExpirationMs);
    }

    public String generateRefreshToken(UserCredentials userCredentials) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userCredentials.getLogin(), jwtRefreshIntervalMs);
    }

    public String createToken(Map<String, Object> claims, String login, long expirationMs) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, Object> validate(String token) {
        if (!validateToken(token)) {
            throw new InvalidTokenException("Invalid or expired token") {};
        }
        return extractAllClaims(token);
    }

    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
