package com.springprojects.dto.invoice;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class InvoiceUpdateRequest {

    @Min(value = 1, message = "Total amount must be at least 1")
    private double totalAmount;

}
