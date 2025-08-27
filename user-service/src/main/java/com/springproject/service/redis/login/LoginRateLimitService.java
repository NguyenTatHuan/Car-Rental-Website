package com.springproject.service.redis.login;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginRateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "auth:login:rate:";
    private static final int MAX_ATTEMPTS = 5;
    private static final long TIMEOUT_MINUTES = 15;

    public LoginRateLimitService(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void incrementFailedAttempts(String username) {
        String key = PREFIX + username;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(TIMEOUT_MINUTES));
        }
    }

    public int getFailedAttempts(String username) {
        String value = redisTemplate.opsForValue().get(PREFIX + username);
        return value != null ? Integer.parseInt(value) : 0;
    }

    public void clearAttempts(String username) {
        redisTemplate.delete(PREFIX + username);
    }

    public boolean isBlocked(String username) {
        return getFailedAttempts(username) >= MAX_ATTEMPTS;
    }

}
