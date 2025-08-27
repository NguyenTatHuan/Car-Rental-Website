package com.springprojects.controller;

import com.springprojects.dto.car.CarCustomerDto;
import com.springprojects.dto.search.CarSearchRequest;
import com.springprojects.elasticsearch.CarDocument;
import com.springprojects.service.customer.car.CustomerCarService;
import com.springprojects.service.elasticsearch.car.CarSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/car")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerCarController {

    private final CustomerCarService customerCarService;

    private final CarSearchService carSearchService;

    @GetMapping
    public ResponseEntity<List<CarCustomerDto>> getAvailableCars() {
        List<CarCustomerDto> cars = customerCarService.getAvailableCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarCustomerDto> getCarDetail(@PathVariable UUID id) {
        CarCustomerDto car = customerCarService.getCarDetail(id);
        return ResponseEntity.ok(car);
    }

    @PostMapping("/search")
    public ResponseEntity<List<CarDocument>> searchCars(@RequestBody CarSearchRequest request) {
        List<CarDocument> results = carSearchService.searchCars(
                request.getLicensePlate(),
                request.getBrandName(),
                request.getModel(),
                request.getCarTypeName(),
                request.getFuelType(),
                request.getTransmissionType(),
                request.getYear(),
                request.getSeats(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getStatus(),
                request.getLocationName(),
                request.getLocationProvince(),
                request.getLocationDistrict()
        );
        return ResponseEntity.ok(results);
    }

}
