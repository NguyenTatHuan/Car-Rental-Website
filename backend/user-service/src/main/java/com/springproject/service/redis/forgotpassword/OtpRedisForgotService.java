package com.springproject.service.redis.forgotpassword;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class OtpRedisForgotService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "auth:forgot:otp:";
    private static final Duration OTP_TTL = Duration.ofMinutes(10);

    public OtpRedisForgotService(@Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveOtp(String username, String otp) {
        redisTemplate.opsForValue().set(PREFIX + username, otp, OTP_TTL);
    }

    public Optional<String> getOtp(String username) {
        String value = redisTemplate.opsForValue().get(PREFIX + username);
        return Optional.ofNullable(value);
    }

    public boolean verifyOtp(String username, String inputOtp) {
        String redisKey = PREFIX + username;
        String savedOtp = redisTemplate.opsForValue().get(redisKey);
        if (savedOtp == null) return false;
        boolean isMatch = savedOtp.equals(inputOtp);
        if (isMatch) {
            redisTemplate.delete(redisKey);
        }
        return isMatch;
    }

    public void deleteOtp(String username) {
        redisTemplate.delete(PREFIX + username);
    }

}
