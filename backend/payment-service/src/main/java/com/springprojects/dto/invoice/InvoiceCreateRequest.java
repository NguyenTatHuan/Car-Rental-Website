package com.springprojects.dto.invoice;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InvoiceCreateRequest {

    @NotNull(message = "Payment ID must not be null")
    private UUID paymentId;

    @Min(value = 1, message = "Total amount must be at least 1")
    private double totalAmount;

}
