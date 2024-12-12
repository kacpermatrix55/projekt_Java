package com.example.demo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final int EXPIRATION_TIME = 30 * 60 * 1000;

    private final SecretKey secretKey;

    private final JwtParser jwtParser;

    public JwtTokenUtil(@Value("${jwt.secret}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts
                .builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    public Claims getClaims(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        String token = authorization.substring(7).stripLeading();

        try {
            return jwtParser
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ignored) {
            return null;
        }
    }
}
