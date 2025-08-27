package com.springprojects.dto.maintenance;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MaintenanceResponseDto {

    private UUID id;

    private UUID carId;

    private String description;

    private Double cost;

    private String servicedBy;

    private LocalDate startDate;

    private LocalDate endDate;

}
