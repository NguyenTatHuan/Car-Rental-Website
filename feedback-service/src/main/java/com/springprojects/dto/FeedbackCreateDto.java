package com.springprojects.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackCreateDto {

    @NotNull(message = "Booking ID must not be null")
    private UUID bookingId;

    @NotNull(message = "User ID must not be null")
    private UUID userId;

    @NotNull(message = "Car ID must not be null")
    private UUID carId;

    @NotNull(message = "Rating must not be null")
    private Double rating;

    @NotBlank(message = "Comment must not be blank")
    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String comment;

}
