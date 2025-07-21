package com.springprojects.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarInformationDto {

    private String carName;

    private double pricePerDay;

}
