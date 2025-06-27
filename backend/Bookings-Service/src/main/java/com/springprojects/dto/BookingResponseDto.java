package com.springprojects.dto;

import com.springprojects.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingResponseDto {

    private UUID id;

    private UUID userId;

    private UUID carId;

    private LocalDate startTime;

    private LocalDate endTime;

    private double totalPrice;

    private BookingStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
