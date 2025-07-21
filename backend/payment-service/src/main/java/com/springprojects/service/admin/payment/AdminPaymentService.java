package com.springprojects.service.admin.payment;

import com.springprojects.dto.payment.PaymentResponse;
import com.springprojects.enums.PaymentMethod;
import com.springprojects.enums.PaymentStatus;

import java.util.List;
import java.util.UUID;

public interface AdminPaymentService {

    List<PaymentResponse> getAllPayments();

    PaymentResponse getPaymentById(UUID paymentId);

    void deletePayment(UUID paymentId);

    void updatePaymentStatus(UUID paymentId, PaymentStatus newStatus);

    void updatePaymentMethod(UUID paymentId, PaymentMethod newMethod);

}
