package com.springprojects.dto.payment;

import com.springprojects.enums.PaymentMethod;
import com.springprojects.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentResponse {

    private UUID id;

    private UUID bookingId;

    private double amount;

    private PaymentStatus status;

    private PaymentMethod method;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
