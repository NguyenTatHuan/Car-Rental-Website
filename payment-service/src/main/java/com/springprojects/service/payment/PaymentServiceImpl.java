package com.springprojects.service.payment;

import com.springprojects.dto.payment.PaymentRequest;
import com.springprojects.entity.Payment;
import com.springprojects.enums.PaymentMethod;
import com.springprojects.enums.PaymentStatus;
import com.springprojects.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public void createPaymentFromBooking(PaymentRequest request) {
        if (paymentRepository.existsByBookingId(request.getBookingId())) return;

        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setAmount(request.getAmount());
        payment.setMethod(PaymentMethod.BANK_TRANSFER);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setDescription("Auto-created payment for booking " + request.getBookingId());
        paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentFromBooking(PaymentRequest request) {
        paymentRepository.findByBookingId(request.getBookingId()).ifPresent(payment -> {
            payment.setAmount(request.getAmount());
            payment.setDescription("Updated payment for booking " + request.getBookingId());
            paymentRepository.save(payment);
        });
    }

    @Override
    public void cancelPaymentFromBooking(PaymentRequest request) {
        paymentRepository.findByBookingId(request.getBookingId()).ifPresent(paymentRepository::delete);
    }

}
