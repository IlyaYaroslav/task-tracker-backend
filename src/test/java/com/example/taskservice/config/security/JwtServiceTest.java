package com.example.taskservice.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    @Test
    void extractsUserIdFromTokenSubject() {
        UUID userId = UUID.randomUUID();
        String token = Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET)), SignatureAlgorithm.HS256)
                .compact();

        JwtService jwtService = new JwtService(SECRET);

        assertThat(jwtService.extractUserId(token)).isEqualTo(userId);
    }
}
