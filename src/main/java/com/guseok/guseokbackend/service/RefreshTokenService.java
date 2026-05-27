package com.guseok.guseokbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String KEY_PREFIX = "refresh:";

    private final RedisTemplate<String, String> redisTemplate;

    public void save(Long memberId, String refreshToken, long expiryMs) {
        redisTemplate.opsForValue()
            .set(key(memberId), refreshToken, expiryMs, TimeUnit.MILLISECONDS);
    }

    public String get(Long memberId) {
        return redisTemplate.opsForValue().get(key(memberId));
    }

    public void delete(Long memberId) {
        redisTemplate.delete(key(memberId));
    }

    public boolean isValid(Long memberId, String refreshToken) {
        String stored = get(memberId);
        return refreshToken.equals(stored);
    }

    private String key(Long memberId) {
        return KEY_PREFIX + memberId;
    }
}