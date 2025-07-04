package com.springprojects.dto.kafka;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingEvent {

    private UUID bookingId;

    private UUID userId;

    private UUID carId;

    private String fullName;

    private String email;

    private String carName;

    private double totalPrice;

    private LocalDate startTime;

    private LocalDate endTime;

}
