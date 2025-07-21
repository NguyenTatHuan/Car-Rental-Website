package com.springproject.service.redis.signup;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IpRateLimitService {

    private static final String PREFIX = "auth:signup:rate:ip:";
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(10);

    private final RedisTemplate<String, String> redisTemplate;

    public IpRateLimitService(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String buildKey(String ip) {
        return PREFIX + ip.trim();
    }

    public boolean isBlocked(String ip) {
        String value = redisTemplate.opsForValue().get(buildKey(ip));
        if (value == null) return false;
        try {
            return Integer.parseInt(value) >= MAX_ATTEMPTS;
        } catch (NumberFormatException e) {
            redisTemplate.delete(buildKey(ip));
            return false;
        }
    }

    public void increaseAttempts(String ip) {
        String key = buildKey(ip);
        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts != null && attempts == 1L) {
            redisTemplate.expire(key, BLOCK_DURATION);
        }
    }

    public void resetAttempts(String ip) {
        redisTemplate.delete(buildKey(ip));
    }

}
