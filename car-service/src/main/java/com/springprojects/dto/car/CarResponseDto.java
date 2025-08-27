package com.springprojects.dto.car;

import com.springprojects.enums.*;
import lombok.Data;

import java.util.UUID;

@Data
public class CarResponseDto {

    private UUID id;

    private String licensePlate;

    private String model;

    private FuelType fuelType;

    private TransmissionType transmissionType;

    private Integer year;

    private Integer seats;

    private String color;

    private Double pricePerDay;

    private String imageUrl;

    private CarStatus status;

    private UUID brandId;

    private String brandName;

    private UUID carTypeId;

    private String carTypeName;

    private UUID locationId;

    private String locationName;

}
