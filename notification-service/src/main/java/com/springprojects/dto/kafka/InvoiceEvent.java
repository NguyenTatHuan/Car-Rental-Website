package com.springprojects.dto.kafka;

import lombok.Data;

import java.util.UUID;

@Data
public class InvoiceEvent {

    private UUID invoiceId;

    private UUID paymentId;

    private double totalAmount;

    private String invoiceCode;

    private double amount;

    private UUID userId;

    private String fullName;

    private String email;

    private String carName;

}
