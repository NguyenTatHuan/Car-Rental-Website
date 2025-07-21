package com.springprojects.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "UUID", insertable = false, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private Payment payment;

    private String invoiceCode;

    private double totalAmount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime issuedDate;

}
