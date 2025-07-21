package com.springproject.service.redis.signup;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class OtpRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "auth:signup:otp:";
    private static final Duration OTP_TTL = Duration.ofMinutes(10);

    public OtpRedisService(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveOtp(String key, String otp) {
        redisTemplate.opsForValue().set(PREFIX + key, otp, OTP_TTL);
    }

    public Optional<String> getOtp(String key) {
        String value = redisTemplate.opsForValue().get(PREFIX + key);
        return Optional.ofNullable(value);
    }

    public boolean verifyOtp(String key, String inputOtp) {
        String redisKey = PREFIX + key;
        String savedOtp = redisTemplate.opsForValue().get(redisKey);
        if (savedOtp == null) return false;
        boolean isMatch = savedOtp.equals(inputOtp);
        if (isMatch) {
            redisTemplate.delete(redisKey);
        }
        return isMatch;
    }

    public void deleteOtp(String key) {
        redisTemplate.delete(PREFIX + key);
    }

}
