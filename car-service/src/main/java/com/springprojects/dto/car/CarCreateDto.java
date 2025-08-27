package com.springprojects.dto.car;

import com.springprojects.enums.CarStatus;
import com.springprojects.enums.FuelType;
import com.springprojects.enums.TransmissionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CarCreateDto {

    @NotBlank(message = "License plate must not be blank")
    @Size(max = 10, message = "License plate must be at most 10 characters")
    private String licensePlate;

    @NotNull(message = "Brand ID must not be null")
    private UUID brandId;

    @NotBlank(message = "Model must not be blank")
    private String model;

    @NotNull(message = "Car type ID must not be null")
    private UUID carTypeId;

    @NotNull(message = "Fuel type must not be null")
    private FuelType fuelType;

    @NotNull(message = "Transmission type must not be null")
    private TransmissionType transmissionType;

    @NotNull(message = "Year must not be null")
    @Min(value = 1900)
    private Integer year;

    @NotNull(message = "Seats must not be null")
    @Min(value = 1)
    private Integer seats;

    @NotBlank(message = "Color must not be blank")
    private String color;

    @NotNull(message = "Price per day must not be null")
    private Double pricePerDay;

    @NotBlank(message = "Image URL must not be blank")
    private String imageUrl;

    @NotNull(message = "Status must not be null")
    private CarStatus status;

    @NotNull(message = "Location ID must not be null")
    private UUID locationId;

}
