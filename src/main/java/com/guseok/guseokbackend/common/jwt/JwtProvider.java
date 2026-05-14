package com.guseok.guseokbackend.common.jwt;

import com.guseok.guseokbackend.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long memberId, String role) {
        Date now = new Date();
        return Jwts.builder()
            .subject(String.valueOf(memberId))
            .claim("role", role)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + jwtProperties.getAccessTokenExpiryMs()))
            .signWith(key)
            .compact();
    }

    public String generateRefreshToken(Long memberId) {
        Date now = new Date();
        return Jwts.builder()
            .subject(String.valueOf(memberId))
            .issuedAt(now)
            .expiration(new Date(now.getTime() + jwtProperties.getRefreshTokenExpiryMs()))
            .signWith(key)
            .compact();
    }

    public long getRefreshTokenExpiryMs() {
        return jwtProperties.getRefreshTokenExpiryMs();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getMemberId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}