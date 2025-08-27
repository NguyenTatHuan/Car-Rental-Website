package com.springprojects.repository.elasticsearch;

import com.springprojects.elasticsearch.CarDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarElasticsearchRepository extends ElasticsearchRepository<CarDocument, UUID> {

    CarDocument findByLicensePlate(String licensePlate);

    List<CarDocument> findByBrandNameContaining(String keyword);

    List<CarDocument> findByModelContaining(String keyword);

    List<CarDocument> findByCarTypeNameContaining(String keyword);

    List<CarDocument> findByFuelType(String fuelType);

    List<CarDocument> findByTransmissionType(String transmissionType);

    List<CarDocument> findByYear(Integer year);

    List<CarDocument> findBySeats(Integer seats);

    List<CarDocument> findByPricePerDayBetween(double minPrice, double maxPrice);

    List<CarDocument> findByStatus(String status);

    List<CarDocument> findByLocationNameContaining(String location);

    List<CarDocument> findByLocationProvinceContaining(String province);

    List<CarDocument> findByLocationDistrictContaining(String district);

}
