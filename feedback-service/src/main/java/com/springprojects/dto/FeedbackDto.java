package com.springprojects.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FeedbackDto {

    private UUID id;

    private UUID bookingId;

    private UUID userId;

    private UUID carId;

    private Double rating;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
