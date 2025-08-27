package com.springprojects.kafka;

import com.springprojects.dto.kafka.BookingEvent;
import com.springprojects.dto.EmailRequestDto;
import com.springprojects.dto.kafka.BookingResponseEvent;
import com.springprojects.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final EmailService emailService;

    private void sendEmail(String to, String subject, String template, Map<String, Object> variables) {
        EmailRequestDto email = new EmailRequestDto(to, subject, template, variables);
        emailService.sendTemplateEmail(email);
    }

    @KafkaListener(
            topics = "booking-created-topic",
            groupId = "notification-group-created",
            containerFactory = "bookingKafkaListenerContainerFactory"
    )
    public void handleBookingCreated(BookingEvent event) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("fullName", event.getFullName());
        vars.put("carName", event.getCarName());
        vars.put("startTime", event.getStartTime());
        vars.put("endTime", event.getEndTime());

        sendEmail(event.getEmail(), "Booking Request Received", "booking-pending", vars);
    }

    @KafkaListener(
            topics = "booking-updated-topic",
            groupId = "notification-group-updated",
            containerFactory = "bookingKafkaListenerContainerFactory"
    )
    public void handleBookingUpdated(BookingEvent event) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("fullName", event.getFullName());
        vars.put("carName", event.getCarName());
        vars.put("startTime", event.getStartTime());
        vars.put("endTime", event.getEndTime());

        sendEmail(event.getEmail(), "Booking Updated Successfully", "booking-updated", vars);
    }

    @KafkaListener(
            topics = "booking-cancelled-topic",
            groupId = "notification-group-cancelled",
            containerFactory = "bookingKafkaListenerContainerFactory"
    )
    public void handleBookingCancelled(BookingEvent event) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("fullName", event.getFullName());
        vars.put("carName", event.getCarName());
        vars.put("startTime", event.getStartTime());
        vars.put("endTime", event.getEndTime());

        sendEmail(event.getEmail(), "Booking Cancelled", "booking-cancelled", vars);
    }

    @KafkaListener(
            topics = "booking-response-topic",
            groupId = "notification-group-response",
            containerFactory = "bookingResponseKafkaListenerContainerFactory"
    )
    public void handleBookingResponse(BookingResponseEvent event) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("fullName", event.getFullName());
        vars.put("carName", event.getCarName());
        vars.put("startTime", event.getStartTime());
        vars.put("endTime", event.getEndTime());
        vars.put("status", event.getStatus());
        vars.put("totalPrice", event.getTotalPrice());

        sendEmail(event.getEmail(), "Booking Response", "booking-response", vars);
    }

}
