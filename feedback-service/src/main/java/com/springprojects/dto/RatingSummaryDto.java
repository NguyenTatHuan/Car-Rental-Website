package com.springprojects.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RatingSummaryDto {

    private UUID carId;

    private double averageRating;

    private int totalRatings;

    private LocalDateTime lastUpdated;

}
