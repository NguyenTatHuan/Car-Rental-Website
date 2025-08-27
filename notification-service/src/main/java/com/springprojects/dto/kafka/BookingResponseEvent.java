package com.springprojects.dto.kafka;

import com.springprojects.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingResponseEvent {

    private UUID bookingId;

    private UUID userId;

    private UUID carId;

    private String fullName;

    private String email;

    private String carName;

    private double totalPrice;

    private BookingStatus status;

    private LocalDate startTime;

    private LocalDate endTime;

}
