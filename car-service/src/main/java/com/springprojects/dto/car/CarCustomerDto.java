package com.springprojects.dto.car;

import com.springprojects.enums.FuelType;
import com.springprojects.enums.TransmissionType;
import lombok.Data;

import java.util.UUID;

@Data
public class CarCustomerDto {

    private UUID id;

    private String model;

    private FuelType fuelType;

    private TransmissionType transmissionType;

    private Integer year;

    private Integer seats;

    private String color;

    private Double pricePerDay;

    private String imageUrl;

    private String brandName;

    private String carTypeName;

    private String locationName;

}
