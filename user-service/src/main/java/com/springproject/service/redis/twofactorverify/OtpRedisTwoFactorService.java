package com.springproject.service.redis.twofactorverify;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class OtpRedisTwoFactorService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "auth:2fa:otp:";
    private static final Duration OTP_TTL = Duration.ofMinutes(10);

    public OtpRedisTwoFactorService(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveOtp(String username, String otp) {
        redisTemplate.opsForValue().set(PREFIX + username, otp, OTP_TTL);
    }

    public Optional<String> getOtp(String username) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + username));
    }

    public boolean verifyOtp(String username, String inputOtp) {
        String redisKey = PREFIX + username;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);
        boolean match = storedOtp != null && storedOtp.equals(inputOtp);
        if (match) redisTemplate.delete(redisKey);
        return match;
    }

    public void deleteOtp(String username) {
        redisTemplate.delete(PREFIX + username);
    }

}
