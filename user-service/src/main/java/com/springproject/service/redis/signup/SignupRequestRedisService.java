package com.springproject.service.redis.signup;

import com.springproject.dto.signup.SignUpRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class SignupRequestRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "auth:signup:request:";
    private static final Duration TTL = Duration.ofMinutes(10);

    public SignupRequestRedisService(@Qualifier("redisTemplateObject") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRequest(String email, SignUpRequest request) {
        redisTemplate.opsForValue().set(PREFIX + email.toLowerCase(), request, TTL);
    }

    public Optional<SignUpRequest> getRequestByEmail(String email) {
        Object data = redisTemplate.opsForValue().get(PREFIX + email.toLowerCase());
        if (data instanceof SignUpRequest request) {
            return Optional.of(request);
        }
        return Optional.empty();
    }

    public void deleteRequest(String email) {
        redisTemplate.delete(PREFIX + email.toLowerCase());
    }

}
