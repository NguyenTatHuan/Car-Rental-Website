package com.springprojects.service.elasticsearch.car;

import com.springprojects.elasticsearch.CarDocument;
import com.springprojects.repository.elasticsearch.CarElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CarSearchServiceImpl implements CarSearchService {

    private final CarElasticsearchRepository carElasticsearchRepository;

    @Override
    public List<CarDocument> searchCars(
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
    ) {
        List<Set<CarDocument>> criteriaResults = new ArrayList<>();

        if (licensePlate != null && !licensePlate.isEmpty()) {
            CarDocument car = carElasticsearchRepository.findByLicensePlate(licensePlate);
            if (car != null) {
                criteriaResults.add(new HashSet<>(Collections.singletonList(car)));
            } else {
                return Collections.emptyList();
            }
        }

        if (brandName != null && !brandName.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByBrandNameContaining(brandName);
            criteriaResults.add(new HashSet<>(list));
        }

        if (model != null && !model.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByModelContaining(model);
            criteriaResults.add(new HashSet<>(list));
        }

        if (carTypeName != null && !carTypeName.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByCarTypeNameContaining(carTypeName);
            criteriaResults.add(new HashSet<>(list));
        }

        if (fuelType != null && !fuelType.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByFuelType(fuelType);
            criteriaResults.add(new HashSet<>(list));
        }

        if (transmissionType != null && !transmissionType.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByTransmissionType(transmissionType);
            criteriaResults.add(new HashSet<>(list));
        }

        if (year != null) {
            List<CarDocument> list = carElasticsearchRepository.findByYear(year);
            criteriaResults.add(new HashSet<>(list));
        }

        if (seats != null) {
            List<CarDocument> list = carElasticsearchRepository.findBySeats(seats);
            criteriaResults.add(new HashSet<>(list));
        }

        if (minPrice != null && maxPrice != null) {
            List<CarDocument> list = carElasticsearchRepository.findByPricePerDayBetween(minPrice, maxPrice);
            criteriaResults.add(new HashSet<>(list));
        } else if (minPrice != null) {
            List<CarDocument> list = carElasticsearchRepository.findByPricePerDayBetween(minPrice, Double.MAX_VALUE);
            criteriaResults.add(new HashSet<>(list));
        } else if (maxPrice != null) {
            List<CarDocument> list = carElasticsearchRepository.findByPricePerDayBetween(0.0, maxPrice);
            criteriaResults.add(new HashSet<>(list));
        }

        if (status != null && !status.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByStatus(status);
            criteriaResults.add(new HashSet<>(list));
        }

        if (locationName != null && !locationName.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByLocationNameContaining(locationName);
            criteriaResults.add(new HashSet<>(list));
        }

        if (locationProvince != null && !locationProvince.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByLocationProvinceContaining(locationProvince);
            criteriaResults.add(new HashSet<>(list));
        }

        if (locationDistrict != null && !locationDistrict.isEmpty()) {
            List<CarDocument> list = carElasticsearchRepository.findByLocationDistrictContaining(locationDistrict);
            criteriaResults.add(new HashSet<>(list));
        }

        if (criteriaResults.isEmpty()) {
            return (List<CarDocument>) carElasticsearchRepository.findAll();
        }

        Set<CarDocument> result = new HashSet<>(criteriaResults.get(0));
        for (int i = 1; i < criteriaResults.size(); i++) {
            result.retainAll(criteriaResults.get(i));
        }

        return new ArrayList<>(result);
    }

}
