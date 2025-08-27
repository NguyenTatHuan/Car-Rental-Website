package com.springprojects.dto.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BookingUpdateDto {

    private UUID carId;

    private LocalDate startTime;

    private LocalDate endTime;

}
