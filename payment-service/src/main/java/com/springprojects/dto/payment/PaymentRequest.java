package com.springprojects.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {

    @NotNull(message = "Booking ID must not be null")
    private UUID bookingId;

    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private double amount;

}
