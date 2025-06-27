package com.springprojects.dto.insurance;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class InsuranceUpdateDto {

    private UUID carId;

    private String provider;

    private Double price;

    private LocalDate startDate;

    private LocalDate endDate;

}
