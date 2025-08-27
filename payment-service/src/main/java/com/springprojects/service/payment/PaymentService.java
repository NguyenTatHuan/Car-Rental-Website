package com.springprojects.service.payment;

import com.springprojects.dto.payment.PaymentRequest;
import com.springprojects.dto.payment.PaymentResponse;

public interface PaymentService {

    void createPaymentFromBooking(PaymentRequest request);

    void updatePaymentFromBooking(PaymentRequest request);

    void cancelPaymentFromBooking(PaymentRequest request);

}
