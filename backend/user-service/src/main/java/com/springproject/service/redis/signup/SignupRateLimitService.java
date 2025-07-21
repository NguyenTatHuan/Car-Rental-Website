package com.springproject.service.redis.signup;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SignupRateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "auth:signup:rate:email:";
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration BLOCK_TIME = Duration.ofMinutes(10);

    public SignupRateLimitService(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String buildKey(String email) {
        return PREFIX + email.toLowerCase();
    }

    public boolean isBlocked(String email) {
        String key = buildKey(email);
        String attemptsStr = redisTemplate.opsForValue().get(key);
        if (attemptsStr == null) return false;
        try {
            int attempts = Integer.parseInt(attemptsStr);
            return attempts >= MAX_ATTEMPTS;
        } catch (NumberFormatException e) {
            redisTemplate.delete(key);
            return false;
        }
    }

    public void increaseAttempts(String email) {
        String key = buildKey(email);
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, "1", BLOCK_TIME);
        } else {
            Long current = redisTemplate.opsForValue().increment(key);
            if (current != null && current == 1L) {
                redisTemplate.expire(key, BLOCK_TIME);
            }
        }
    }

    public void resetAttempts(String email) {
        redisTemplate.delete(buildKey(email));
    }

}
