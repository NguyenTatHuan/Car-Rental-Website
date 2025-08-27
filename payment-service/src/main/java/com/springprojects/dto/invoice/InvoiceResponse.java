package com.springprojects.dto.invoice;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InvoiceResponse {

    private UUID id;

    private UUID paymentId;

    private String invoiceCode;

    private double totalAmount;

    private LocalDateTime issuedDate;

}
