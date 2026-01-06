package com.AttendanceManagementSystem.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static String SECRET;
    private static long EXPIRATION_TIME;
    private static Key SECRET_KEY;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        SECRET = secret;
        EXPIRATION_TIME = expiration;
        SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public static String generateToken(String userId, String role, String name){
        return Jwts.builder()
            .setSubject(userId)
            .setClaims(Map.of("role", role, "name", name))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

    public static Claims validateToken(String token){
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
