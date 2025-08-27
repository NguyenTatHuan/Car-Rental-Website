package com.springprojects.kafka;

import com.springprojects.dto.kafka.BookingEvent;
import com.springprojects.dto.kafka.BookingResponseEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String BOOKING_CREATED_TOPIC = "booking-created-topic";
    private static final String BOOKING_UPDATED_TOPIC = "booking-updated-topic";
    private static final String BOOKING_CANCELLED_TOPIC = "booking-cancelled-topic";
    private static final String BOOKING_RESPONSE_TOPIC = "booking-response-topic";

    public void sendBookingCreatedEvent(BookingEvent event) {
        kafkaTemplate.send(BOOKING_CREATED_TOPIC, event.getBookingId().toString(), event);
    }

    public void sendBookingUpdatedEvent(BookingEvent event) {
        kafkaTemplate.send(BOOKING_UPDATED_TOPIC, event.getBookingId().toString(), event);
    }

    public void sendBookingCancelledEvent(BookingEvent event) {
        kafkaTemplate.send(BOOKING_CANCELLED_TOPIC, event.getBookingId().toString(), event);
    }

    public void sendBookingResponseEvent(BookingResponseEvent event) {
        kafkaTemplate.send(BOOKING_RESPONSE_TOPIC, event.getBookingId().toString(), event);
    }

}
