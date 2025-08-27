package com.springprojects.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarSearchRequest {

    private String licensePlate;

    private String brandName;

    private String model;

    private String carTypeName;

    private String fuelType;

    private String transmissionType;

    private Integer year;

    private Integer seats;

    private Double minPrice;

    private Double maxPrice;

    private String status;

    private String locationName;

    private String locationProvince;

    private String locationDistrict;

}
