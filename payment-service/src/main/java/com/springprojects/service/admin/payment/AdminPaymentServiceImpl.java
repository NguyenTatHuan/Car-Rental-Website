package com.springprojects.service.admin.payment;

import com.springprojects.dto.payment.PaymentResponse;
import com.springprojects.entity.Payment;
import com.springprojects.enums.PaymentMethod;
import com.springprojects.enums.PaymentStatus;
import com.springprojects.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final PaymentRepository paymentRepository;

    private PaymentResponse mapToDto(Payment payment) {
        PaymentResponse dto = new PaymentResponse();
        dto.setId(payment.getId());
        dto.setBookingId(payment.getBookingId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setMethod(payment.getMethod());
        dto.setDescription(payment.getDescription());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found payment with ID: " + paymentId));
        return mapToDto(payment);
    }

    @Override
    public void deletePayment(UUID paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found payment with ID: " + paymentId);
        }
        paymentRepository.deleteById(paymentId);
    }

    @Override
    public void updatePaymentStatus(UUID paymentId, PaymentStatus newStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found payment with ID: " + paymentId));
        PaymentStatus currentStatus = payment.getStatus();
        if (currentStatus == newStatus) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment is already in status: " + newStatus);
        }
        if (currentStatus == PaymentStatus.SUCCESS && newStatus != PaymentStatus.REFUNDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment with status SUCCESS can only be changed to REFUND.");
        }
        if (currentStatus == PaymentStatus.REFUNDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change status after REFUND.");
        }
        payment.setStatus(newStatus);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentMethod(UUID paymentId, PaymentMethod newMethod) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found payment with ID: " + paymentId));
        if (payment.getMethod() == newMethod) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment already uses method: " + newMethod);
        }
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change method for a completed payment.");
        }
        payment.setMethod(newMethod);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

}
