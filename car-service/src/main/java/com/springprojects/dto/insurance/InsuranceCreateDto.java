package com.springprojects.dto.insurance;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class InsuranceCreateDto {

    @NotNull(message = "Car ID must not be null")
    private UUID carId;

    @NotNull(message = "Provider must not be null")
    private String provider;

    @NotNull(message = "Price must not be null")
    private Double price;

    @NotNull(message = "Start date must not be null")
    private LocalDate startDate;

    @NotNull(message = "End date must not be null")
    private LocalDate endDate;

}
