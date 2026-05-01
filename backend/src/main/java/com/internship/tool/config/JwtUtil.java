package com.internship.tool.config;

import com.internship.tool.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "mysecretkeymysecretkeymysecretkey";
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // =========================
    // GENERATE TOKEN
    // =========================
    public String generateToken(User user) {

        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User or email cannot be null");
        }

        String role = (user.getRole() != null)
                ? user.getRole().name()
                : "USER"; // fallback

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // EXTRACT USERNAME
    // =========================
    public String extractUsername(String token) {
        return extractClaimsSafe(token).getSubject();
    }

    // =========================
    // EXTRACT ROLE
    // =========================
    public String extractRole(String token) {
        return extractClaimsSafe(token).get("role", String.class);
    }

    // =========================
    // EXTRACT ALL CLAIMS (SAFE)
    // =========================
    private Claims extractClaimsSafe(String token) {

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired");

        } catch (UnsupportedJwtException |
                 MalformedJwtException |
                 SecurityException |
                 IllegalArgumentException e) {

            throw new RuntimeException("Invalid token");
        }
    }

    // =========================
    // VALIDATE TOKEN
    // =========================
    public boolean validateToken(String token, String username) {

        if (token == null || username == null) {
            return false;
        }

        try {
            return username.equals(extractUsername(token)) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // CHECK EXPIRATION
    // =========================
    private boolean isTokenExpired(String token) {
        return extractClaimsSafe(token).getExpiration().before(new Date());
    }
}