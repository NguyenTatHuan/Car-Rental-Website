package com.springprojects.kafka;

import com.springprojects.dto.kafka.BookingEvent;
import com.springprojects.dto.payment.PaymentRequest;
import com.springprojects.service.payment.PaymentService;
import com.springprojects.service.redis.BookingCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final PaymentService paymentService;

    private final BookingCacheService bookingCacheService;

    @KafkaListener(
            topics = "booking-created-topic",
            groupId = "payment-service-group",
            containerFactory = "bookingKafkaListenerContainerFactory"
    )
    public void handleBookingCreated(BookingEvent event) {
        bookingCacheService.saveBookingEvent(event);
        PaymentRequest request = new PaymentRequest();
        request.setBookingId(event.getBookingId());
        request.setAmount(event.getTotalPrice());
        paymentService.createPaymentFromBooking(request);
    }

    @KafkaListener(
            topics = "booking-updated-topic",
            groupId = "payment-service-group",
            containerFactory = "bookingKafkaListenerContainerFactory"
    )
    public void handleBookingUpdated(BookingEvent event) {
        bookingCacheService.saveBookingEvent(event);
        PaymentRequest request = new PaymentRequest();
        request.setBookingId(event.getBookingId());
        request.setAmount(event.getTotalPrice());
        paymentService.updatePaymentFromBooking(request);
    }

    @KafkaListener(
            topics = "booking-cancelled-topic",
            groupId = "payment-service-group",
            containerFactory = "bookingKafkaListenerContainerFactory"
    )
    public void handleBookingCancelled(BookingEvent event) {
        bookingCacheService.deleteBookingEvent(event.getBookingId());
        PaymentRequest request = new PaymentRequest();
        request.setBookingId(event.getBookingId());
        paymentService.cancelPaymentFromBooking(request);
    }

}
