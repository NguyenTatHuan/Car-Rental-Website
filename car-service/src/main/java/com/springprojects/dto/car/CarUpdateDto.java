package com.springprojects.dto.car;

import com.springprojects.enums.CarStatus;
import com.springprojects.enums.FuelType;
import com.springprojects.enums.TransmissionType;
import lombok.Data;

import java.util.UUID;

@Data
public class CarUpdateDto {

    private String licensePlate;

    private UUID brandId;

    private String model;

    private UUID carTypeId;

    private FuelType fuelType;

    private TransmissionType transmissionType;

    private Integer year;

    private Integer seats;

    private String color;

    private Double pricePerDay;

    private String imageUrl;

    private CarStatus status;

    private UUID locationId;

}
