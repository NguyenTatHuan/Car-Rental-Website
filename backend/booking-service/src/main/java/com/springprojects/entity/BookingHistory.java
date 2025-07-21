package com.springprojects.entity;

import com.springprojects.enums.BookingStatus;
import com.springprojects.enums.HistoryAction;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "booking_history")
public class BookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "UUID", insertable = false, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID bookingId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID carId;

    @Column(nullable = false)
    private LocalDate startTime;

    @Column(nullable = false)
    private LocalDate endTime;

    @Column(nullable = false)
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HistoryAction action;

    @CreationTimestamp
    private LocalDateTime changedAt;

}
