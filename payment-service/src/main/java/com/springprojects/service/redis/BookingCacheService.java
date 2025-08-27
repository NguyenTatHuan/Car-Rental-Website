package com.springprojects.service.redis;

import com.springprojects.dto.kafka.BookingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingCacheService {

    private final RedisTemplate<String, BookingEvent> redisTemplate;

    private static final Duration TTL = Duration.ofDays(15);

    private String getKey(UUID bookingId) {
        return "booking:" + bookingId;
    }

    public void saveBookingEvent(BookingEvent bookingEvent) {
        redisTemplate.opsForValue().set(getKey(bookingEvent.getBookingId()), bookingEvent, TTL);
    }

    public BookingEvent getBookingEvent(UUID bookingId) {
        return redisTemplate.opsForValue().get(getKey(bookingId));
    }

    public void deleteBookingEvent(UUID bookingId) {
        redisTemplate.delete(getKey(bookingId));
    }

}
