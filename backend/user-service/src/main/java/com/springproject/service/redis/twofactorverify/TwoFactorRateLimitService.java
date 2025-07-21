package com.springproject.service.redis.twofactorverify;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TwoFactorRateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String ATTEMPT_PREFIX = "auth:otp:verify2fa:fail:";
    private static final String LOCK_PREFIX = "auth:otp:verify2fa:lock:";
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration ATTEMPT_TTL = Duration.ofMinutes(15);
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    public TwoFactorRateLimitService(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void increaseOtpAttempt(String username) {
        String key = ATTEMPT_PREFIX + username;
        Long current = redisTemplate.opsForValue().increment(key);
        if (current != null && current == 1) {
            redisTemplate.expire(key, ATTEMPT_TTL);
        }
    }

    public boolean hasExceededMaxAttempts(String username) {
        String key = ATTEMPT_PREFIX + username;
        String value = redisTemplate.opsForValue().get(key);
        return value != null && Integer.parseInt(value) >= MAX_ATTEMPTS;
    }

    public void lockUser(String username) {
        String key = LOCK_PREFIX + username;
        redisTemplate.opsForValue().set(key, "LOCKED", LOCK_DURATION);
    }

    public boolean isUserLocked(String username) {
        String key = LOCK_PREFIX + username;
        return redisTemplate.hasKey(key);
    }

    public void clearOtpAttempts(String username) {
        redisTemplate.delete(ATTEMPT_PREFIX + username);
    }

    public void unlockUser(String username) {
        redisTemplate.delete(LOCK_PREFIX + username);
    }

}
