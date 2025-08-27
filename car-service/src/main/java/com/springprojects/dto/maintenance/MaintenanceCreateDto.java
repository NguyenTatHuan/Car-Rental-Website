package com.springprojects.dto.maintenance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MaintenanceCreateDto {

    @NotNull(message = "Car ID must not be null")
    private UUID carId;

    private String description;

    @NotNull(message = "Cost must not be null")
    private double cost;

    @NotBlank(message = "ServicedBy must not be blank")
    @Size(max = 100, message = "ServicedBy must not exceed 100 characters")
    private String servicedBy;

    @NotNull(message = "Start date must not be null")
    private LocalDate startDate;

    @NotNull(message = "End date must not be null")
    private LocalDate endDate;

}
