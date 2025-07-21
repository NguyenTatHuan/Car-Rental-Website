package com.springprojects.dto;

import com.springprojects.enums.BookingStatus;
import com.springprojects.enums.HistoryAction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingHistoryResponseDto {

    private UUID id;

    private UUID bookingId;

    private UUID userId;

    private UUID carId;

    private LocalDate startTime;

    private LocalDate endTime;

    private double totalPrice;

    private BookingStatus status;

    private HistoryAction action;

    private LocalDateTime changedAt;

}
