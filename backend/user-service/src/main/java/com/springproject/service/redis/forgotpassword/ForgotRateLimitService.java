package com.springproject.service.redis.forgotpassword;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ForgotRateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "auth:forgot:rate:";
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration TTL = Duration.ofMinutes(15);

    public ForgotRateLimitService(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void increaseAttempts(String username) {
        String key = PREFIX + username;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, TTL);
        }
    }

    public boolean isBlocked(String username) {
        String value = redisTemplate.opsForValue().get(PREFIX + username);
        if (value == null) return false;
        try {
            return Integer.parseInt(value) >= MAX_ATTEMPTS;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void resetAttempts(String username) {
        redisTemplate.delete(PREFIX + username);
    }

}
