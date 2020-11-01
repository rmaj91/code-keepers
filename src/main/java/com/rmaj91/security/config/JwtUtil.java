package com.rmaj91.security.config;

import com.rmaj91.security.domain.JwtResponse;
import com.rmaj91.security.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static final int MILLISECONDS_MULTIPLIER = 1000;

    @Value("${security.jwt.secretKey}")
    private String secretKey;
    @Value("${security.jwt.expirationTimeSec}")
    private String expirationTimeSec;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtResponse generateJwtResponse(User user) {
        Long expirationTimeSecLong = Long.parseLong(expirationTimeSec);
        final Date expiration = new Date(new Date().getTime() + expirationTimeSecLong * MILLISECONDS_MULTIPLIER);
        String token = Jwts.builder()
                .setClaims(Map.of("role", user.getRole()))
                .setSubject(user.getUsername())
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        return JwtResponse.builder()
                .accessToken(token)
                .tokenType("JWT")
                .expiredAt(expiration)
                .build();
    }
}
