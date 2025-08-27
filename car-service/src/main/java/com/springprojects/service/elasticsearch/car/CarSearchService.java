package com.springprojects.service.elasticsearch.car;

import com.springprojects.elasticsearch.CarDocument;

import java.util.List;

public interface CarSearchService {

    List<CarDocument> searchCars(
            String licensePlate,
            String brandName,
            String model,
            String carTypeName,
            String fuelType,
            String transmissionType,
            Integer year,
            Integer seats,
            Double minPrice,
            Double maxPrice,
            String status,
            String locationName,
            String locationProvince,
            String locationDistrict
    );

}
